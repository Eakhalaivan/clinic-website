package com.healthcare.clinic.doctor.service;

import com.healthcare.clinic.doctor.entity.BillingOutbox;
import com.healthcare.clinic.doctor.repository.BillingOutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClinicalBillingService {

    private final BillingOutboxRepository billingOutboxRepository;

    @Transactional
    public BillingOutbox createBillingEvent(Long encounterId, Long patientId, Long doctorId, String serviceType, String serviceCode, BigDecimal amount) {
        BillingOutbox outbox = new BillingOutbox();
        outbox.setEncounterId(encounterId);
        outbox.setPatientId(patientId);
        outbox.setDoctorId(doctorId);
        outbox.setServiceType(serviceType);
        outbox.setServiceCode(serviceCode);
        outbox.setAmount(amount);
        outbox.setStatus("Pending");
        
        return billingOutboxRepository.save(outbox);
    }

    public List<BillingOutbox> getPendingBillingEvents() {
        return billingOutboxRepository.findByStatus("Pending");
    }

    @Transactional
    public void markAsProcessed(Long outboxId) {
        BillingOutbox outbox = billingOutboxRepository.findById(outboxId)
                .orElseThrow(() -> new RuntimeException("Billing outbox record not found"));
        outbox.setStatus("Processed");
        outbox.setProcessedAt(ZonedDateTime.now());
        billingOutboxRepository.save(outbox);
    }

    @Transactional
    public void markAsFailed(Long outboxId, String errorMessage) {
        BillingOutbox outbox = billingOutboxRepository.findById(outboxId)
                .orElseThrow(() -> new RuntimeException("Billing outbox record not found"));
        outbox.setStatus("Failed");
        outbox.setErrorMessage(errorMessage);
        outbox.setProcessedAt(ZonedDateTime.now());
        billingOutboxRepository.save(outbox);
    }
}
