package com.healthcare.clinic.doctor.service;

import com.healthcare.clinic.doctor.repository.PrescriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ClinicPrescriptionSyncService {

    private final PrescriptionRepository clinicalPrescriptionRepository;

    @Transactional(transactionManager = "clinicTransactionManager")
    public void syncClinicalStatus(Long clinicalPrescriptionId, String newStatus, String performedBy, LocalDateTime dispensedAt) {
        if (clinicalPrescriptionId == null) return;
        clinicalPrescriptionRepository.findById(clinicalPrescriptionId).ifPresent(clinical -> {
            clinical.setPharmacyStatus(newStatus);
            if ("DISPENSED".equals(newStatus)) {
                clinical.setDispensedAt(dispensedAt);
                clinical.setDispensedBy(performedBy);
            }
            clinicalPrescriptionRepository.save(clinical);
        });
    }
}
