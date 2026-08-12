package com.healthcare.clinic.marketing.repository;

import com.healthcare.clinic.marketing.entity.CampaignSegment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampaignSegmentRepository extends JpaRepository<CampaignSegment, Long> {
    List<CampaignSegment> findByBranchIdAndIsActiveTrue(Long branchId);
    List<CampaignSegment> findByIsPublicTrueAndIsActiveTrue();
}
