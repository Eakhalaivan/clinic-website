package com.healthcare.clinic.marketing.repository;

import com.healthcare.clinic.marketing.entity.MarketingCampaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketingCampaignRepository extends JpaRepository<MarketingCampaign, Long> {
}
