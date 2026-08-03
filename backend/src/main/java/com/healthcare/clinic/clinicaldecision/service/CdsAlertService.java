package com.healthcare.clinic.clinicaldecision.service;

import com.healthcare.clinic.clinicaldecision.entity.AlertStatus;
import com.healthcare.clinic.clinicaldecision.entity.CdsAlert;
import com.healthcare.clinic.clinicaldecision.repository.CdsAlertRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CdsAlertService {

    private final CdsAlertRepository alertRepository;

    @Transactional(readOnly = true)
    public List<CdsAlert> getAlertsForPatient(Long patientId) {
        return alertRepository.findByPatientIdOrderByCreatedAtDesc(patientId);
    }

    @Transactional(readOnly = true)
    public List<CdsAlert> getPendingAlerts() {
        return alertRepository.findByStatusOrderByCreatedAtDesc(AlertStatus.PENDING);
    }

    @Transactional
    public CdsAlert acknowledgeAlert(Long alertId, String overrideReason) {
        CdsAlert alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new RuntimeException("CDS Alert not found: " + alertId));
        if (overrideReason != null && !overrideReason.isBlank()) {
            alert.setStatus(AlertStatus.OVERRIDDEN);
            alert.setOverrideReason(overrideReason);
        } else {
            alert.setStatus(AlertStatus.ACKNOWLEDGED);
        }
        alert.setAcknowledgedAt(ZonedDateTime.now());
        return alertRepository.save(alert);
    }

    /**
     * Persists a CDS alert in a separate transaction (REQUIRES_NEW) so audit log remains
     * even if the primary business transaction rolls back due to a CRITICAL safety violation.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public CdsAlert saveAlertInNewTransaction(CdsAlert alert) {
        return alertRepository.save(alert);
    }
}
