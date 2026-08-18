package com.healthcare.clinic.marketing.controller;

import com.healthcare.clinic.marketing.entity.*;
import com.healthcare.clinic.marketing.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

/**
 * Marketing CRM Controller — all endpoints require real authorization,
 * and all destructive actions enforce consent + approval rules server-side.
 */
@RestController
@RequestMapping("/api/marketing")
@RequiredArgsConstructor
public class MarketingController {

    private final MarketingService marketingService;       // legacy compat
    private final CampaignService campaignService;
    private final ConsentService consentService;
    private final SegmentService segmentService;
    private final LeadService leadService;
    private final LoyaltyService loyaltyService;
    private final NpsService npsService;
    private final CouponService couponService;
    private final GiftCardService giftCardService;
    private final MarketingDashboardService dashboardService;
    private final CommunicationHistoryService communicationHistoryService;

    // ─── Communications ───────────────────────────────────────────────────────

    @PostMapping("/communications/send")
    @PreAuthorize("hasAnyRole('MARKETING','SUPER_ADMIN')")
    public ResponseEntity<CommunicationHistory> sendCommunication(@RequestBody CommunicationHistory communication) {
        return ResponseEntity.ok(communicationHistoryService.sendCommunication(communication));
    }

    @GetMapping("/communications/history")
    @PreAuthorize("hasAnyRole('MARKETING','SUPER_ADMIN','RECEPTION')")
    public ResponseEntity<Page<CommunicationHistory>> getCommunicationHistory(
            @RequestParam Long patientId,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(communicationHistoryService.getPatientCommunicationHistory(patientId, pageable));
    }

    // ─── Dashboard ────────────────────────────────────────────────────────────

    @GetMapping("/dashboard")
    @PreAuthorize("hasAnyRole('MARKETING','SUPER_ADMIN','BRANCH_ADMIN')")
    public ResponseEntity<Map<String, Object>> getDashboard(@RequestParam(required = false) Long branchId) {
        return ResponseEntity.ok(dashboardService.getDashboardMetrics(branchId));
    }

    // ─── Campaign Lifecycle ───────────────────────────────────────────────────

    @GetMapping("/campaigns")
    @PreAuthorize("hasAnyRole('MARKETING','SUPER_ADMIN','BRANCH_ADMIN')")
    public ResponseEntity<List<Campaign>> getCampaigns() {
        return ResponseEntity.ok(campaignService.getAllCampaigns());
    }

    @GetMapping("/campaigns/{id}")
    @PreAuthorize("hasAnyRole('MARKETING','SUPER_ADMIN','BRANCH_ADMIN')")
    public ResponseEntity<Campaign> getCampaign(@PathVariable Long id) {
        return ResponseEntity.ok(campaignService.getById(id));
    }

    @PostMapping("/campaigns")
    @PreAuthorize("hasAnyRole('MARKETING','SUPER_ADMIN')")
    public ResponseEntity<Campaign> createCampaign(@RequestBody Campaign campaign,
                                                    @RequestParam Long ownerId,
                                                    @RequestParam Long branchId) {
        return ResponseEntity.ok(campaignService.createCampaign(campaign, ownerId, branchId));
    }

    @PostMapping("/campaigns/{id}/submit")
    @PreAuthorize("hasAnyRole('MARKETING','SUPER_ADMIN')")
    public ResponseEntity<Campaign> submitForReview(@PathVariable Long id) {
        return ResponseEntity.ok(campaignService.submitForReview(id));
    }

    @PostMapping("/campaigns/{id}/approve")
    @PreAuthorize("hasAnyRole('MARKETING','SUPER_ADMIN')")
    public ResponseEntity<Campaign> approveCampaign(@PathVariable Long id,
                                                     @RequestParam Long approvedBy) {
        return ResponseEntity.ok(campaignService.approveCampaign(id, approvedBy));
    }

    @PostMapping("/campaigns/{id}/schedule")
    @PreAuthorize("hasAnyRole('MARKETING','SUPER_ADMIN')")
    public ResponseEntity<Campaign> scheduleCampaign(@PathVariable Long id,
                                                      @RequestParam ZonedDateTime scheduledAt) {
        return ResponseEntity.ok(campaignService.scheduleCampaign(id, scheduledAt));
    }

    @PostMapping("/campaigns/{id}/activate")
    @PreAuthorize("hasAnyRole('MARKETING','SUPER_ADMIN')")
    public ResponseEntity<Campaign> activateCampaign(@PathVariable Long id) {
        return ResponseEntity.ok(campaignService.activateCampaign(id));
    }

    @PostMapping("/campaigns/{id}/pause")
    @PreAuthorize("hasAnyRole('MARKETING','SUPER_ADMIN')")
    public ResponseEntity<Campaign> pauseCampaign(@PathVariable Long id) {
        return ResponseEntity.ok(campaignService.pauseCampaign(id));
    }

    @PostMapping("/campaigns/{id}/resume")
    @PreAuthorize("hasAnyRole('MARKETING','SUPER_ADMIN')")
    public ResponseEntity<Campaign> resumeCampaign(@PathVariable Long id) {
        return ResponseEntity.ok(campaignService.resumeCampaign(id));
    }

    @PostMapping("/campaigns/{id}/cancel")
    @PreAuthorize("hasAnyRole('MARKETING','SUPER_ADMIN')")
    public ResponseEntity<Campaign> cancelCampaign(@PathVariable Long id) {
        return ResponseEntity.ok(campaignService.cancelCampaign(id));
    }

    @PostMapping("/campaigns/{id}/complete")
    @PreAuthorize("hasAnyRole('MARKETING','SUPER_ADMIN')")
    public ResponseEntity<Campaign> completeCampaign(@PathVariable Long id) {
        return ResponseEntity.ok(campaignService.completeCampaign(id));
    }

    @PostMapping("/campaigns/{id}/archive")
    @PreAuthorize("hasAnyRole('MARKETING','SUPER_ADMIN')")
    public ResponseEntity<Campaign> archiveCampaign(@PathVariable Long id) {
        return ResponseEntity.ok(campaignService.archiveCampaign(id));
    }

    @PostMapping("/campaigns/{id}/clone")
    @PreAuthorize("hasAnyRole('MARKETING','SUPER_ADMIN')")
    public ResponseEntity<Campaign> cloneCampaign(@PathVariable Long id, @RequestParam Long clonedBy) {
        return ResponseEntity.ok(campaignService.cloneCampaign(id, clonedBy));
    }

    @GetMapping("/campaigns/{id}/analytics")
    @PreAuthorize("hasAnyRole('MARKETING','SUPER_ADMIN','BRANCH_ADMIN')")
    public ResponseEntity<Map<String, Object>> getCampaignAnalytics(@PathVariable Long id) {
        return ResponseEntity.ok(campaignService.getCampaignAnalytics(id));
    }

    // ─── Consent ─────────────────────────────────────────────────────────────

    @PostMapping("/consent")
    @PreAuthorize("hasAnyRole('MARKETING','SUPER_ADMIN','RECEPTION','PATIENT')")
    public ResponseEntity<MarketingConsent> captureConsent(
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) Long leadId,
            @RequestParam String channel,
            @RequestParam(required = false) String consentSource,
            @RequestParam(required = false) String wordingVersion,
            @RequestParam(required = false) String purpose,
            @RequestParam(required = false) String ipAddress,
            @RequestParam(required = false) Long branchId,
            @RequestParam(required = false) Long operatorId) {
        return ResponseEntity.ok(consentService.captureConsent(
                patientId, leadId, channel, consentSource, wordingVersion, purpose, ipAddress, branchId, operatorId));
    }

    @PostMapping("/consent/withdraw")
    @PreAuthorize("hasAnyRole('MARKETING','SUPER_ADMIN','RECEPTION','PATIENT')")
    public ResponseEntity<MarketingConsent> withdrawConsent(
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) Long leadId,
            @RequestParam String channel,
            @RequestParam(required = false) String operatorId,
            @RequestParam(required = false) Long branchId) {
        return ResponseEntity.ok(consentService.withdrawConsent(patientId, leadId, channel, operatorId, branchId));
    }

    @GetMapping("/consent/patient/{patientId}")
    @PreAuthorize("hasAnyRole('MARKETING','SUPER_ADMIN','PATIENT')")
    public ResponseEntity<List<MarketingConsent>> getConsentHistory(@PathVariable Long patientId) {
        return ResponseEntity.ok(consentService.getConsentHistoryForPatient(patientId));
    }

    // ─── Segments ────────────────────────────────────────────────────────────

    @GetMapping("/segments")
    @PreAuthorize("hasAnyRole('MARKETING','SUPER_ADMIN')")
    public ResponseEntity<List<CampaignSegment>> getSegments(@RequestParam(required = false) Long branchId) {
        return ResponseEntity.ok(segmentService.listSegmentsForBranch(branchId));
    }

    @PostMapping("/segments")
    @PreAuthorize("hasAnyRole('MARKETING','SUPER_ADMIN')")
    public ResponseEntity<CampaignSegment> createSegment(@RequestBody CampaignSegment segment,
                                                          @RequestParam Long createdBy) {
        return ResponseEntity.ok(segmentService.createSegment(segment, createdBy));
    }

    @GetMapping("/segments/{id}/count")
    @PreAuthorize("hasAnyRole('MARKETING','SUPER_ADMIN')")
    public ResponseEntity<Map<String, Integer>> getSegmentCount(@PathVariable Long id) {
        return ResponseEntity.ok(Map.of("count", segmentService.countAudience(id)));
    }

    @GetMapping("/segments/{id}/preview")
    @PreAuthorize("hasAnyRole('MARKETING','SUPER_ADMIN')")
    public ResponseEntity<Page<Map<String, Object>>> previewSegment(
            @PathVariable Long id,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(segmentService.previewAudience(id, pageable));
    }

    // ─── Leads ───────────────────────────────────────────────────────────────

    @GetMapping("/leads")
    @PreAuthorize("hasAnyRole('MARKETING','SUPER_ADMIN','RECEPTION')")
    public ResponseEntity<Page<Lead>> getLeads(@RequestParam(required = false) Long branchId,
                                                @RequestParam(required = false) String status,
                                                @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(leadService.getPipeline(branchId, status, pageable));
    }

    @PostMapping("/leads")
    @PreAuthorize("hasAnyRole('MARKETING','SUPER_ADMIN','RECEPTION')")
    public ResponseEntity<Lead> createLead(@RequestBody Lead lead) {
        return ResponseEntity.ok(leadService.createOrGetExistingLead(lead));
    }

    @GetMapping("/leads/{id}")
    @PreAuthorize("hasAnyRole('MARKETING','SUPER_ADMIN','RECEPTION')")
    public ResponseEntity<Lead> getLead(@PathVariable Long id) {
        return ResponseEntity.ok(leadService.getById(id));
    }

    @PutMapping("/leads/{id}/status")
    @PreAuthorize("hasAnyRole('MARKETING','SUPER_ADMIN','RECEPTION')")
    public ResponseEntity<Lead> updateLeadStatus(@PathVariable Long id,
                                                  @RequestParam String status,
                                                  @RequestParam Long performedBy,
                                                  @RequestParam(required = false) String notes) {
        return ResponseEntity.ok(leadService.transitionStatus(id, status, performedBy, notes));
    }

    @PostMapping("/leads/{id}/activities")
    @PreAuthorize("hasAnyRole('MARKETING','SUPER_ADMIN','RECEPTION')")
    public ResponseEntity<LeadActivity> addActivity(@PathVariable Long id,
                                                     @RequestBody LeadActivity activity) {
        return ResponseEntity.ok(leadService.addActivity(id, activity));
    }

    @GetMapping("/leads/{id}/activities")
    @PreAuthorize("hasAnyRole('MARKETING','SUPER_ADMIN','RECEPTION')")
    public ResponseEntity<List<LeadActivity>> getActivities(@PathVariable Long id) {
        return ResponseEntity.ok(leadService.getActivities(id));
    }

    @PostMapping("/leads/{id}/convert")
    @PreAuthorize("hasAnyRole('MARKETING','SUPER_ADMIN')")
    public ResponseEntity<Lead> convertToPatient(@PathVariable Long id,
                                                  @RequestParam Long patientId,
                                                  @RequestParam Long performedBy) {
        return ResponseEntity.ok(leadService.convertToPatient(id, patientId, performedBy));
    }

    // ─── Loyalty ─────────────────────────────────────────────────────────────

    @GetMapping("/loyalty/{patientId}")
    @PreAuthorize("hasAnyRole('MARKETING','SUPER_ADMIN','RECEPTION','PATIENT')")
    public ResponseEntity<PatientLoyalty> getLoyalty(@PathVariable Long patientId) {
        return ResponseEntity.ok(loyaltyService.getLoyaltyBalance(patientId));
    }

    @GetMapping("/loyalty/{patientId}/transactions")
    @PreAuthorize("hasAnyRole('MARKETING','SUPER_ADMIN','PATIENT')")
    public ResponseEntity<Page<LoyaltyTransaction>> getLoyaltyHistory(
            @PathVariable Long patientId,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(loyaltyService.getTransactionHistory(patientId, pageable));
    }

    @PostMapping("/loyalty/award")
    @PreAuthorize("hasAnyRole('MARKETING','SUPER_ADMIN','RECEPTION')")
    public ResponseEntity<LoyaltyTransaction> awardPoints(@RequestParam Long patientId,
                                                            @RequestParam int points,
                                                            @RequestParam String referenceType,
                                                            @RequestParam(required = false) Long referenceId,
                                                            @RequestParam(required = false) String idempotencyKey) {
        return ResponseEntity.ok(loyaltyService.awardPoints(patientId, points, referenceType, referenceId, idempotencyKey));
    }

    @PostMapping("/loyalty/redeem")
    @PreAuthorize("hasAnyRole('MARKETING','SUPER_ADMIN','RECEPTION')")
    public ResponseEntity<LoyaltyTransaction> redeemPoints(@RequestParam Long patientId,
                                                             @RequestParam int points,
                                                             @RequestParam Long invoiceId,
                                                             @RequestParam(required = false) String idempotencyKey) {
        return ResponseEntity.ok(loyaltyService.redeemPoints(patientId, points, invoiceId, idempotencyKey));
    }

    @PostMapping("/loyalty/adjust")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','MARKETING')")
    public ResponseEntity<LoyaltyTransaction> adjustPoints(@RequestParam Long patientId,
                                                            @RequestParam int points,
                                                            @RequestParam String notes,
                                                            @RequestParam Long approvedBy) {
        return ResponseEntity.ok(loyaltyService.manualAdjust(patientId, points, notes, approvedBy));
    }

    // ─── Coupons ─────────────────────────────────────────────────────────────

    @GetMapping("/coupons")
    @PreAuthorize("hasAnyRole('MARKETING','SUPER_ADMIN','RECEPTION')")
    public ResponseEntity<List<Coupon>> getCoupons() {
        return ResponseEntity.ok(marketingService.getAllCoupons());
    }

    @PostMapping("/coupons")
    @PreAuthorize("hasAnyRole('MARKETING','SUPER_ADMIN')")
    public ResponseEntity<Coupon> createCoupon(@RequestBody Coupon coupon, @RequestParam Long createdBy) {
        return ResponseEntity.ok(couponService.createCoupon(coupon, createdBy));
    }

    @PostMapping("/coupons/{id}/approve")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','MARKETING')")
    public ResponseEntity<Coupon> approveCoupon(@PathVariable Long id, @RequestParam Long approvedBy) {
        return ResponseEntity.ok(couponService.approveCoupon(id, approvedBy));
    }

    @PostMapping("/coupons/validate")
    @PreAuthorize("hasAnyRole('MARKETING','SUPER_ADMIN','RECEPTION','BILLING')")
    public ResponseEntity<Map<String, Object>> validateCoupon(
            @RequestParam String code,
            @RequestParam Long patientId,
            @RequestParam java.math.BigDecimal orderAmount,
            @RequestParam(required = false) Long invoiceId,
            @RequestParam(required = false) Long branchId) {
        java.math.BigDecimal discount = couponService.validateAndApply(code, patientId, orderAmount, invoiceId, branchId);
        return ResponseEntity.ok(Map.of("discountApplied", discount));
    }

    // ─── Gift Cards ───────────────────────────────────────────────────────────

    @PostMapping("/gift-cards/issue")
    @PreAuthorize("hasAnyRole('MARKETING','SUPER_ADMIN','RECEPTION')")
    public ResponseEntity<Map<String, Object>> issueGiftCard(
            @RequestParam java.math.BigDecimal amount,
            @RequestParam Long issuedToPatientId,
            @RequestParam(required = false) Long purchasedByPatientId,
            @RequestParam(required = false) Long purchaseInvoiceId,
            @RequestParam(required = false) Long branchId,
            @RequestParam(required = false) ZonedDateTime expiresAt) {
        GiftCardService.GiftCardIssueResult result = giftCardService.issueGiftCard(
                amount, issuedToPatientId, purchasedByPatientId, purchaseInvoiceId, branchId, expiresAt);
        // Return plaintext code ONCE — after this call it cannot be retrieved again
        return ResponseEntity.ok(Map.of(
                "giftCard", result.card(),
                "code", result.plainCode() // one-time display
        ));
    }

    @GetMapping("/gift-cards/balance")
    @PreAuthorize("hasAnyRole('MARKETING','SUPER_ADMIN','RECEPTION','PATIENT')")
    public ResponseEntity<GiftCard> getGiftCardBalance(@RequestParam String code) {
        return ResponseEntity.ok(giftCardService.getBalance(code));
    }

    @PostMapping("/gift-cards/redeem")
    @PreAuthorize("hasAnyRole('MARKETING','SUPER_ADMIN','RECEPTION','BILLING')")
    public ResponseEntity<GiftCard> redeemGiftCard(
            @RequestParam String code,
            @RequestParam java.math.BigDecimal amount,
            @RequestParam(required = false) Long invoiceId) {
        return ResponseEntity.ok(giftCardService.redeem(code, amount, invoiceId));
    }

    // ─── NPS / Feedback ───────────────────────────────────────────────────────

    @GetMapping("/nps/surveys")
    @PreAuthorize("hasAnyRole('MARKETING','SUPER_ADMIN','BRANCH_ADMIN')")
    public ResponseEntity<Page<NpsSurvey>> getNpsSurveys(
            @RequestParam Long branchId,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(npsService.getSurveysForBranch(branchId, pageable));
    }

    @PostMapping("/nps/surveys")
    @PreAuthorize("hasAnyRole('MARKETING','SUPER_ADMIN','DOCTOR','RECEPTION')")
    public ResponseEntity<NpsSurvey> createSurvey(
            @RequestParam String eventType,
            @RequestParam Long eventId,
            @RequestParam Long patientId,
            @RequestParam(required = false) Long branchId,
            @RequestParam(required = false) Long serviceId,
            @RequestParam(required = false) Long doctorId) {
        return ResponseEntity.ok(npsService.createSurveyForEvent(eventType, eventId, patientId, branchId, serviceId, doctorId));
    }

    @PostMapping("/nps/surveys/{id}/respond")
    @PreAuthorize("hasAnyRole('PATIENT','SUPER_ADMIN')")
    public ResponseEntity<NpsResponse> submitResponse(
            @PathVariable Long id,
            @RequestParam(required = false) Integer npsScore,
            @RequestParam(required = false) Integer rating,
            @RequestParam(required = false) String comments,
            @RequestParam(required = false) String category) {
        return ResponseEntity.ok(npsService.submitResponse(id, npsScore, rating, comments, category));
    }
    
    @PostMapping("/nps/surveys/{id}/resolve")
    @PreAuthorize("hasAnyRole('MARKETING','SUPER_ADMIN','BRANCH_ADMIN')")
    public ResponseEntity<NpsResponse> resolveEscalation(
            @PathVariable Long id,
            @RequestParam Long resolvedBy,
            @RequestParam String resolutionNotes) {
        return ResponseEntity.ok(npsService.resolveEscalation(id, resolvedBy, resolutionNotes));
    }

    @GetMapping("/nps/metrics")
    @PreAuthorize("hasAnyRole('MARKETING','SUPER_ADMIN','BRANCH_ADMIN')")
    public ResponseEntity<Map<String, Object>> getNpsMetrics(@RequestParam Long branchId) {
        Double avg = npsService.getAverageNpsForBranch(branchId);
        return ResponseEntity.ok(Map.of("averageNpsScore", avg != null ? avg : 0.0, "branchId", branchId));
    }

    // ─── Legacy endpoints (backward compat) ──────────────────────────────────

    @GetMapping("/referrals")
    @PreAuthorize("hasAnyRole('MARKETING','SUPER_ADMIN')")
    public ResponseEntity<List<Referral>> getReferrals() {
        return ResponseEntity.ok(marketingService.getAllReferrals());
    }

    @PostMapping("/referrals")
    @PreAuthorize("hasAnyRole('MARKETING','SUPER_ADMIN','PATIENT')")
    public ResponseEntity<Referral> createReferral(@RequestBody Referral referral) {
        return ResponseEntity.ok(marketingService.createReferral(referral));
    }
}
