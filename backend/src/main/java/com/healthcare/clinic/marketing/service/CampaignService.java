package com.healthcare.clinic.marketing.service;

import com.healthcare.clinic.marketing.entity.*;
import com.healthcare.clinic.marketing.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CampaignService {

    private final CampaignRepository campaignRepository;
    private final CampaignDeliveryRepository deliveryRepository;
    private final ConsentService consentService;
    private final CampaignSegmentRepository segmentRepository;

    @Transactional(readOnly = true)
    public List<Campaign> getAllCampaigns() {
        return campaignRepository.findAllByOrderByCreatedAtDesc();
    }

    @Transactional(readOnly = true)
    public Campaign getById(Long id) {
        return campaignRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Campaign not found: " + id));
    }

    @Transactional
    public Campaign createCampaign(Campaign campaign, Long ownerId, Long branchId) {
        campaign.setOwnerId(ownerId);
        campaign.setBranchId(branchId);
        campaign.setStatus("DRAFT");
        campaign.setSentCount(0);
        return campaignRepository.save(campaign);
    }

    /**
     * Submit campaign for review. Only DRAFT campaigns can be submitted.
     */
    @Transactional
    public Campaign submitForReview(Long campaignId) {
        Campaign campaign = getById(campaignId);
        if (!"DRAFT".equals(campaign.getStatus())) {
            throw new IllegalStateException("Only DRAFT campaigns can be submitted for review");
        }
        campaign.setStatus("REVIEW");
        return campaignRepository.save(campaign);
    }

    /**
     * Approve campaign. Requires MARKETING_MANAGER or SUPER_ADMIN role (enforced in controller).
     */
    @Transactional
    public Campaign approveCampaign(Long campaignId, Long approvedBy) {
        Campaign campaign = getById(campaignId);
        if (!"REVIEW".equals(campaign.getStatus())) {
            throw new IllegalStateException("Only campaigns in REVIEW can be approved");
        }
        campaign.setStatus("APPROVED");
        campaign.setApprovedBy(approvedBy);
        campaign.setApprovedAt(ZonedDateTime.now());
        return campaignRepository.save(campaign);
    }

    /**
     * Schedule campaign. Only APPROVED campaigns can be scheduled.
     */
    @Transactional
    public Campaign scheduleCampaign(Long campaignId, ZonedDateTime scheduledAt) {
        Campaign campaign = getById(campaignId);
        if (!"APPROVED".equals(campaign.getStatus())) {
            throw new IllegalStateException("Only APPROVED campaigns can be scheduled");
        }
        campaign.setStatus("SCHEDULED");
        campaign.setScheduledAt(scheduledAt);
        return campaignRepository.save(campaign);
    }

    /**
     * Activate campaign (start sending). Validates consent segment is configured.
     * Prevents send without approval, valid segment, and at least one channel configured.
     */
    @Transactional
    public Campaign activateCampaign(Long campaignId) {
        Campaign campaign = getById(campaignId);
        if (!"SCHEDULED".equals(campaign.getStatus()) && !"APPROVED".equals(campaign.getStatus())) {
            throw new IllegalStateException("Campaign must be APPROVED or SCHEDULED to activate");
        }
        if (campaign.getChannels() == null || campaign.getChannels().isEmpty()) {
            throw new IllegalStateException("Campaign must have at least one channel configured");
        }
        if (campaign.getTargetSegmentId() == null) {
            throw new IllegalStateException("Campaign must have a target segment");
        }
        // Validate segment exists
        segmentRepository.findById(campaign.getTargetSegmentId())
                .orElseThrow(() -> new IllegalStateException("Target segment not found"));

        campaign.setStatus("ACTIVE");
        return campaignRepository.save(campaign);
    }

    @Transactional
    public Campaign pauseCampaign(Long campaignId) {
        Campaign campaign = getById(campaignId);
        if (!"ACTIVE".equals(campaign.getStatus())) {
            throw new IllegalStateException("Only ACTIVE campaigns can be paused");
        }
        campaign.setStatus("PAUSED");
        return campaignRepository.save(campaign);
    }

    @Transactional
    public Campaign resumeCampaign(Long campaignId) {
        Campaign campaign = getById(campaignId);
        if (!"PAUSED".equals(campaign.getStatus())) {
            throw new IllegalStateException("Only PAUSED campaigns can be resumed");
        }
        campaign.setStatus("ACTIVE");
        return campaignRepository.save(campaign);
    }

    @Transactional
    public Campaign cancelCampaign(Long campaignId) {
        Campaign campaign = getById(campaignId);
        if ("COMPLETED".equals(campaign.getStatus()) || "ARCHIVED".equals(campaign.getStatus())) {
            throw new IllegalStateException("Cannot cancel a " + campaign.getStatus() + " campaign");
        }
        campaign.setStatus("CANCELLED");
        return campaignRepository.save(campaign);
    }

    @Transactional
    public Campaign completeCampaign(Long campaignId) {
        Campaign campaign = getById(campaignId);
        if (!"ACTIVE".equals(campaign.getStatus()) && !"PAUSED".equals(campaign.getStatus())) {
            throw new IllegalStateException("Campaign must be ACTIVE or PAUSED to complete");
        }
        campaign.setStatus("COMPLETED");
        return campaignRepository.save(campaign);
    }

    @Transactional
    public Campaign archiveCampaign(Long campaignId) {
        Campaign campaign = getById(campaignId);
        if (!"COMPLETED".equals(campaign.getStatus()) && !"CANCELLED".equals(campaign.getStatus())) {
            throw new IllegalStateException("Only COMPLETED or CANCELLED campaigns can be archived");
        }
        campaign.setStatus("ARCHIVED");
        campaign.setArchivedAt(ZonedDateTime.now());
        return campaignRepository.save(campaign);
    }

    /**
     * Clone a campaign back to DRAFT state.
     */
    @Transactional
    public Campaign cloneCampaign(Long campaignId, Long clonedBy) {
        Campaign original = getById(campaignId);
        Campaign clone = Campaign.builder()
                .title("Copy of " + original.getTitle())
                .campaignType(original.getCampaignType())
                .objective(original.getObjective())
                .channels(original.getChannels())
                .contentTemplateId(original.getContentTemplateId())
                .content(original.getContent())
                .budget(original.getBudget())
                .ownerId(clonedBy)
                .branchId(original.getBranchId())
                .targetSegmentId(original.getTargetSegmentId())
                .frequencyCapPerUser(original.getFrequencyCapPerUser())
                .status("DRAFT")
                .sentCount(0)
                .targetAudience(original.getTargetAudience())
                .build();
        return campaignRepository.save(clone);
    }

    /**
     * Returns real analytics for a campaign.
     */
    @Transactional(readOnly = true)
    public Map<String, Object> getCampaignAnalytics(Long campaignId) {
        getById(campaignId); // existence check

        List<Object[]> statusCounts = deliveryRepository.countByStatusForCampaign(campaignId);
        Map<String, Long> countsByStatus = new java.util.LinkedHashMap<>();
        for (Object[] row : statusCounts) {
            countsByStatus.put((String) row[0], (Long) row[1]);
        }

        return Map.of(
                "campaignId", campaignId,
                "sent", countsByStatus.getOrDefault("SENT", 0L),
                "delivered", countsByStatus.getOrDefault("DELIVERED", 0L),
                "opened", countsByStatus.getOrDefault("OPENED", 0L),
                "clicked", countsByStatus.getOrDefault("CLICKED", 0L),
                "bounced", countsByStatus.getOrDefault("BOUNCED", 0L),
                "unsubscribed", countsByStatus.getOrDefault("UNSUBSCRIBED", 0L),
                "failed", countsByStatus.getOrDefault("FAILED", 0L)
        );
    }
}
