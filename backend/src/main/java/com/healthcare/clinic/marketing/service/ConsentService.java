package com.healthcare.clinic.marketing.service;

import com.healthcare.clinic.marketing.entity.MarketingConsent;
import com.healthcare.clinic.marketing.repository.MarketingConsentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsentService {

    private final MarketingConsentRepository consentRepository;

    /**
     * Captures explicit marketing consent for a channel.
     * Separates marketing consent from clinical/transactional notifications.
     */
    @Transactional
    public MarketingConsent captureConsent(Long patientId, Long leadId, String channel,
                                           String consentSource, String wordingVersion,
                                           String purpose, String ipAddress, Long branchId, Long operatorId) {
        MarketingConsent consent = MarketingConsent.builder()
                .patientId(patientId)
                .leadId(leadId)
                .channel(channel)
                .consentState("OPTED_IN")
                .consentSource(consentSource)
                .wordingVersion(wordingVersion)
                .purpose(purpose)
                .capturedAt(ZonedDateTime.now())
                .ipAddress(ipAddress)
                .branchId(branchId)
                .operatorId(operatorId)
                .build();
        return consentRepository.save(consent);
    }

    /**
     * Withdraws consent by recording OPTED_OUT state. Previous records are preserved as audit trail.
     */
    @Transactional
    public MarketingConsent withdrawConsent(Long patientId, Long leadId, String channel,
                                            String operatorId_str, Long branchId) {
        Long operatorId = operatorId_str != null ? Long.parseLong(operatorId_str) : null;
        MarketingConsent withdrawal = MarketingConsent.builder()
                .patientId(patientId)
                .leadId(leadId)
                .channel(channel)
                .consentState("OPTED_OUT")
                .consentSource("WITHDRAWAL")
                .capturedAt(ZonedDateTime.now())
                .withdrawnAt(ZonedDateTime.now())
                .branchId(branchId)
                .operatorId(operatorId)
                .build();
        return consentRepository.save(withdrawal);
    }

    /**
     * Checks if a patient has active marketing consent for a given channel.
     * Returns true only if the most recent consent record is OPTED_IN and not expired.
     */
    @Transactional(readOnly = true)
    public boolean hasActiveConsent(Long patientId, String channel) {
        return consentRepository
                .findTopByPatientIdAndChannelOrderByCapturedAtDesc(patientId, channel)
                .map(c -> "OPTED_IN".equals(c.getConsentState())
                        && (c.getExpiresAt() == null || c.getExpiresAt().isAfter(ZonedDateTime.now())))
                .orElse(false);
    }

    /**
     * Checks if a lead has active marketing consent for a given channel.
     */
    @Transactional(readOnly = true)
    public boolean hasActiveConsentForLead(Long leadId, String channel) {
        return consentRepository
                .findTopByLeadIdAndChannelOrderByCapturedAtDesc(leadId, channel)
                .map(c -> "OPTED_IN".equals(c.getConsentState())
                        && (c.getExpiresAt() == null || c.getExpiresAt().isAfter(ZonedDateTime.now())))
                .orElse(false);
    }

    @Transactional(readOnly = true)
    public List<MarketingConsent> getConsentHistoryForPatient(Long patientId) {
        return consentRepository.findByPatientIdOrderByCapturedAtDesc(patientId);
    }

    @Transactional(readOnly = true)
    public List<MarketingConsent> getConsentHistoryForLead(Long leadId) {
        return consentRepository.findByLeadIdOrderByCapturedAtDesc(leadId);
    }
}
