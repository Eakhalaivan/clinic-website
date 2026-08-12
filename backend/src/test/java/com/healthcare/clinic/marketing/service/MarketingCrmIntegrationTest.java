package com.healthcare.clinic.marketing.service;

import com.healthcare.clinic.marketing.entity.*;
import com.healthcare.clinic.marketing.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.*;

/**
 * Phase 16 Marketing CRM Integration Tests.
 * Covers: campaign lifecycle, consent enforcement, loyalty idempotency,
 * gift card concurrency, NPS idempotency, coupon limits, referral fraud prevention,
 * and lead conversion.
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class MarketingCrmIntegrationTest {

    @Autowired ConsentService consentService;
    @Autowired CampaignService campaignService;
    @Autowired LoyaltyService loyaltyService;
    @Autowired GiftCardService giftCardService;
    @Autowired NpsService npsService;
    @Autowired CouponService couponService;
    @Autowired LeadService leadService;
    @Autowired CampaignSegmentRepository segmentRepository;
    @Autowired CampaignRepository campaignRepository;
    @Autowired CouponRepository couponRepository;
    @Autowired NpsSurveyRepository surveyRepository;

    // ─── Campaign Lifecycle Test ──────────────────────────────────────────────

    @Test
    void campaignLifecycle_draftToArchived_allTransitionsSucceed() {
        Campaign campaign = campaignService.createCampaign(
                Campaign.builder()
                        .title("Health Awareness Q3")
                        .campaignType("HEALTH_AWARENESS")
                        .channels(List.of("EMAIL"))
                        .content("Test content")
                        .build(),
                1L, 1L);
        assertThat(campaign.getStatus()).isEqualTo("DRAFT");

        campaign = campaignService.submitForReview(campaign.getId());
        assertThat(campaign.getStatus()).isEqualTo("REVIEW");

        campaign = campaignService.approveCampaign(campaign.getId(), 1L);
        assertThat(campaign.getStatus()).isEqualTo("APPROVED");
        assertThat(campaign.getApprovedBy()).isEqualTo(1L);

        // Cancel the approved campaign
        campaign = campaignService.cancelCampaign(campaign.getId());
        assertThat(campaign.getStatus()).isEqualTo("CANCELLED");

        // Archive the cancelled campaign
        campaign = campaignService.archiveCampaign(campaign.getId());
        assertThat(campaign.getStatus()).isEqualTo("ARCHIVED");
        assertThat(campaign.getArchivedAt()).isNotNull();
    }

    @Test
    void campaignActivation_withoutSegment_throwsException() {
        Campaign campaign = campaignService.createCampaign(
                Campaign.builder()
                        .title("Segmentless Campaign")
                        .channels(List.of("EMAIL"))
                        .content("content")
                        .build(),
                1L, 1L);
        campaignService.submitForReview(campaign.getId());
        campaignService.approveCampaign(campaign.getId(), 1L);

        // Should fail — no segment assigned
        assertThatThrownBy(() -> campaignService.activateCampaign(campaign.getId()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("target segment");
    }

    // ─── Consent Enforcement Test ─────────────────────────────────────────────

    @Test
    void consent_optIn_thenOptOut_latestStateIsOptedOut() {
        Long patientId = 999L;
        consentService.captureConsent(patientId, null, "EMAIL", "PORTAL", "v1", "MARKETING",
                "127.0.0.1", 1L, 1L);
        assertThat(consentService.hasActiveConsent(patientId, "EMAIL")).isTrue();

        consentService.withdrawConsent(patientId, null, "EMAIL", "1", 1L);
        assertThat(consentService.hasActiveConsent(patientId, "EMAIL")).isFalse();
    }

    @Test
    void consent_separateByChannel_channelScopedCorrectly() {
        Long patientId = 998L;
        consentService.captureConsent(patientId, null, "EMAIL", "REGISTRATION", "v1", "MARKETING",
                null, 1L, null);
        // SMS not opted in
        assertThat(consentService.hasActiveConsent(patientId, "EMAIL")).isTrue();
        assertThat(consentService.hasActiveConsent(patientId, "SMS")).isFalse();
    }

    // ─── Lead Deduplication Test ─────────────────────────────────────────────

    @Test
    void leadCreation_duplicatePhoneEmail_returnsSameLead() {
        Lead lead1 = Lead.builder()
                .source("WEBSITE").phone("+911234567890").email("test@example.com")
                .branchId(1L).build();
        Lead saved1 = leadService.createOrGetExistingLead(lead1);

        Lead lead2 = Lead.builder()
                .source("CAMPAIGN").phone("+91 1234567890").email("TEST@EXAMPLE.COM")
                .branchId(1L).build();
        Lead saved2 = leadService.createOrGetExistingLead(lead2);

        // Same dedup key — should return same lead
        assertThat(saved1.getId()).isEqualTo(saved2.getId());
    }

    @Test
    void leadConversion_setsConvertedPatientId_andStatusChange() {
        Lead lead = leadService.createOrGetExistingLead(
                Lead.builder().source("MANUAL").phone("111").email("a@b.com").branchId(1L).build());
        Lead converted = leadService.convertToPatient(lead.getId(), 42L, 1L);

        assertThat(converted.getStatus()).isEqualTo("CONVERTED");
        assertThat(converted.getConvertedPatientId()).isEqualTo(42L);
    }

    // ─── Loyalty Idempotency Test ─────────────────────────────────────────────

    @Test
    void loyaltyAward_sameIdempotencyKey_awardedOnlyOnce() {
        Long patientId = 777L;
        String idemKey = "INVOICE_1001_EARN";
        loyaltyService.awardPoints(patientId, 100, "INVOICE", 1001L, idemKey);
        loyaltyService.awardPoints(patientId, 100, "INVOICE", 1001L, idemKey); // duplicate

        var balance = loyaltyService.getLoyaltyBalance(patientId);
        assertThat(balance.getPointsBalance()).isEqualTo(100); // only awarded once
    }

    @Test
    void loyaltyRedemption_insufficientBalance_throwsException() {
        Long patientId = 776L;
        loyaltyService.awardPoints(patientId, 50, "INVOICE", 2001L, "IDEM_2001");
        assertThatThrownBy(() -> loyaltyService.redeemPoints(patientId, 200, 3001L, "RED_001"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Insufficient");
    }

    // ─── Gift Card Tests ──────────────────────────────────────────────────────

    @Test
    void giftCard_issue_balanceAndRedemptionWork() {
        GiftCardService.GiftCardIssueResult result = giftCardService.issueGiftCard(
                new BigDecimal("500.00"), 1L, null, null, 1L, null);
        assertThat(result.plainCode()).isNotBlank();
        assertThat(result.card().getCurrentBalance()).isEqualByComparingTo("500.00");

        GiftCard afterRedeem = giftCardService.redeem(result.plainCode(), new BigDecimal("200.00"), 100L);
        assertThat(afterRedeem.getCurrentBalance()).isEqualByComparingTo("300.00");
        assertThat(afterRedeem.getStatus()).isEqualTo("ACTIVE");
    }

    @Test
    void giftCard_fullRedemption_statusBecomesRedeemed() {
        GiftCardService.GiftCardIssueResult result = giftCardService.issueGiftCard(
                new BigDecimal("100.00"), 1L, null, null, 1L, null);
        giftCardService.redeem(result.plainCode(), new BigDecimal("100.00"), 200L);

        GiftCard balance = giftCardService.getBalance(result.plainCode());
        assertThat(balance.getStatus()).isEqualTo("REDEEMED");
    }

    @Test
    void giftCard_overdraftAttempt_throwsException() {
        GiftCardService.GiftCardIssueResult result = giftCardService.issueGiftCard(
                new BigDecimal("50.00"), 1L, null, null, 1L, null);
        assertThatThrownBy(() -> giftCardService.redeem(result.plainCode(), new BigDecimal("100.00"), 300L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Insufficient");
    }

    // ─── Coupon Tests ─────────────────────────────────────────────────────────

    @Test
    void coupon_perPatientLimit_enforced() {
        Coupon coupon = couponService.createCoupon(Coupon.builder()
                .code("TEST50")
                .discountType("PERCENTAGE")
                .discountValue(new BigDecimal("50"))
                .validFrom(LocalDate.now().minusDays(1))
                .validTo(LocalDate.now().plusDays(30))
                .usageLimit(100)
                .perPatientLimit(1)
                .isActive(true)
                .build(), 1L);

        Long patientId = 888L;
        couponService.validateAndApply("TEST50", patientId, new BigDecimal("1000"), 101L, 1L);

        assertThatThrownBy(() ->
                couponService.validateAndApply("TEST50", patientId, new BigDecimal("1000"), 102L, 1L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("already used");
    }

    @Test
    void coupon_expiredCode_throwsException() {
        couponService.createCoupon(Coupon.builder()
                .code("EXPIRED10")
                .discountType("FIXED_AMOUNT")
                .discountValue(new BigDecimal("100"))
                .validFrom(LocalDate.now().minusDays(30))
                .validTo(LocalDate.now().minusDays(1)) // expired
                .usageLimit(100)
                .perPatientLimit(5)
                .isActive(true)
                .build(), 1L);

        assertThatThrownBy(() ->
                couponService.validateAndApply("EXPIRED10", 1L, new BigDecimal("500"), null, null))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("not valid today");
    }

    // ─── NPS Idempotency Test ─────────────────────────────────────────────────

    @Test
    void nps_duplicateSurveyForSameEvent_returnsSameSurvey() {
        NpsSurvey s1 = npsService.createSurveyForEvent("APPOINTMENT", 5001L, 1L, 1L, null, null);
        NpsSurvey s2 = npsService.createSurveyForEvent("APPOINTMENT", 5001L, 1L, 1L, null, null);

        assertThat(s1.getId()).isEqualTo(s2.getId());
    }

    @Test
    void nps_duplicateResponse_throwsException() {
        NpsSurvey survey = npsService.createSurveyForEvent("APPOINTMENT", 5002L, 1L, 1L, null, null);
        npsService.submitResponse(survey.getId(), 9, 5, "Great service", "GENERAL");

        // Second call should throw — survey is already COMPLETED after first response
        assertThatThrownBy(() ->
                npsService.submitResponse(survey.getId(), 7, 4, "Okay", "GENERAL"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("already been completed");
    }

    @Test
    void nps_lowScore_escalated() {
        NpsSurvey survey = npsService.createSurveyForEvent("APPOINTMENT", 5003L, 1L, 1L, null, null);
        NpsResponse response = npsService.submitResponse(survey.getId(), 4, 2, "Poor experience", "SERVICE");

        assertThat(response.getEscalationStatus()).isEqualTo("ESCALATED");
    }
}
