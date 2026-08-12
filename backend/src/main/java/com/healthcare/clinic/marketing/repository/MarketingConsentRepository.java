package com.healthcare.clinic.marketing.repository;

import com.healthcare.clinic.marketing.entity.MarketingConsent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MarketingConsentRepository extends JpaRepository<MarketingConsent, Long> {

    List<MarketingConsent> findByPatientIdOrderByCapturedAtDesc(Long patientId);

    List<MarketingConsent> findByLeadIdOrderByCapturedAtDesc(Long leadId);

    Optional<MarketingConsent> findTopByPatientIdAndChannelOrderByCapturedAtDesc(Long patientId, String channel);

    Optional<MarketingConsent> findTopByLeadIdAndChannelOrderByCapturedAtDesc(Long leadId, String channel);
}
