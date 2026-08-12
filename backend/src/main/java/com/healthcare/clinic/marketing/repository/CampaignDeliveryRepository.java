package com.healthcare.clinic.marketing.repository;

import com.healthcare.clinic.marketing.entity.CampaignDelivery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CampaignDeliveryRepository extends JpaRepository<CampaignDelivery, Long> {

    Page<CampaignDelivery> findByCampaignId(Long campaignId, Pageable pageable);

    Optional<CampaignDelivery> findByIdempotencyKey(String idempotencyKey);

    @Query("SELECT COUNT(d) FROM CampaignDelivery d WHERE d.campaignId = :campaignId AND d.status = :status")
    long countByCampaignIdAndStatus(@Param("campaignId") Long campaignId, @Param("status") String status);

    @Query("""
        SELECT d.status, COUNT(d)
        FROM CampaignDelivery d
        WHERE d.campaignId = :campaignId
        GROUP BY d.status
        """)
    java.util.List<Object[]> countByStatusForCampaign(@Param("campaignId") Long campaignId);
}
