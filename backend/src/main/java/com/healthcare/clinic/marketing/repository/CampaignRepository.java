package com.healthcare.clinic.marketing.repository;

import com.healthcare.clinic.marketing.entity.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Long> {
    List<Campaign> findAllByOrderByCreatedAtDesc();
    List<Campaign> findByStatus(String status);
    List<Campaign> findByBranchIdAndStatus(Long branchId, String status);
    long countByStatus(String status);
    long countByBranchIdAndStatus(Long branchId, String status);
}
