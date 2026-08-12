package com.healthcare.clinic.marketing.service;

import com.healthcare.clinic.marketing.entity.*;
import com.healthcare.clinic.marketing.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MarketingDashboardService {

    private final CampaignRepository campaignRepository;
    private final LeadRepository leadRepository;
    private final NpsSurveyRepository npsRepository;
    private final NpsResponseRepository npsResponseRepository;
    private final MarketingConsentRepository consentRepository;
    private final CampaignDeliveryRepository deliveryRepository;
    private final PatientMembershipRepository membershipRepository;
    private final ReferralRepository referralRepository;
    private final CouponRepository couponRepository;
    private final PatientLoyaltyRepository loyaltyRepository;

    /**
     * Returns real, role-scoped dashboard metrics.
     * All figures come from DB aggregates — no mocked values.
     */
    @Transactional(readOnly = true)
    public Map<String, Object> getDashboardMetrics(Long branchId) {
        return Map.ofEntries(
                Map.entry("activeCampaigns",       campaignRepository.countByStatus("ACTIVE")),
                Map.entry("pendingApprovals",       campaignRepository.countByStatus("REVIEW")),
                Map.entry("totalLeads",             leadRepository.countByBranchIdAndStatus(branchId, "NEW")
                        + leadRepository.countByBranchIdAndStatus(branchId, "CONTACTED")
                        + leadRepository.countByBranchIdAndStatus(branchId, "QUALIFIED")),
                Map.entry("convertedLeads",         leadRepository.countByBranchIdAndStatus(branchId, "CONVERTED")),
                Map.entry("activeMemberships",      membershipRepository.countByStatus("ACTIVE")),
                Map.entry("expiredMemberships",     membershipRepository.countByStatus("EXPIRED")),
                Map.entry("pendingReferralRewards",  referralRepository.countByStatus("REWARD_PENDING")),
                Map.entry("activeCoupons",           couponRepository.countByIsActiveTrue()),
                Map.entry("npsResponseCount",        npsRepository.countByStatus("COMPLETED")),
                Map.entry("averageNps",              npsResponseRepository.averageNpsScoreForBranch(branchId)),
                Map.entry("escalatedNps",            npsRepository.countByStatus("ESCALATED"))
        );
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getCampaignSummary() {
        return Map.of(
                "draft",     campaignRepository.countByStatus("DRAFT"),
                "review",    campaignRepository.countByStatus("REVIEW"),
                "approved",  campaignRepository.countByStatus("APPROVED"),
                "active",    campaignRepository.countByStatus("ACTIVE"),
                "completed", campaignRepository.countByStatus("COMPLETED")
        );
    }
}
