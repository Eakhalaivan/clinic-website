package com.healthcare.clinic.marketing.repository;

import com.healthcare.clinic.marketing.entity.CommunicationHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunicationHistoryRepository extends JpaRepository<CommunicationHistory, Long> {
    Page<CommunicationHistory> findByPatientIdOrderByEventTimestampDesc(Long patientId, Pageable pageable);
    Page<CommunicationHistory> findByLeadIdOrderByEventTimestampDesc(Long leadId, Pageable pageable);
    Page<CommunicationHistory> findByCampaignIdOrderByEventTimestampDesc(Long campaignId, Pageable pageable);
}
