package com.healthcare.clinic.marketing.repository;

import com.healthcare.clinic.marketing.entity.CampaignAutomation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampaignAutomationRepository extends JpaRepository<CampaignAutomation, Long> {
    List<CampaignAutomation> findByTriggerTypeAndStatus(String triggerType, String status);
    List<CampaignAutomation> findByStatus(String status);
}
