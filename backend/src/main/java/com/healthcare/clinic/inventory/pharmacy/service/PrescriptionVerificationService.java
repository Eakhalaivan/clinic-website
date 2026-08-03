package com.healthcare.clinic.inventory.pharmacy.service;

import com.healthcare.clinic.inventory.entity.PharmacyPrescriptionRecord;
import com.healthcare.clinic.inventory.pharmacy.exception.ResourceNotFoundException;
import com.healthcare.clinic.inventory.pharmacy.repository.PrescriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service("pharmacyPrescriptionVerificationService")
public class PrescriptionVerificationService {

    private final PrescriptionRepository prescriptionRepository;
    private final com.healthcare.clinic.doctor.repository.PrescriptionRepository clinicalPrescriptionRepository;

    public PrescriptionVerificationService(
            PrescriptionRepository prescriptionRepository,
            com.healthcare.clinic.doctor.repository.PrescriptionRepository clinicalPrescriptionRepository) {
        this.prescriptionRepository = prescriptionRepository;
        this.clinicalPrescriptionRepository = clinicalPrescriptionRepository;
    }

    @Transactional
    public PharmacyPrescriptionRecord verifyPrescription(Long id, String pharmacistUsername) {
        PharmacyPrescriptionRecord p = prescriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prescription not found"));

        p.setVerificationStatus("VERIFIED");
        p.setVerifiedBy(pharmacistUsername);
        p.setVerifiedAt(LocalDateTime.now());
        PharmacyPrescriptionRecord saved = prescriptionRepository.save(p);

        // Sync back to clinical record if linked
        syncClinicalStatus(saved, "VERIFIED", pharmacistUsername);
        return saved;
    }

    @Transactional
    public PharmacyPrescriptionRecord rejectPrescription(Long id, String reason, String pharmacistUsername) {
        PharmacyPrescriptionRecord p = prescriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prescription not found"));

        p.setVerificationStatus("REJECTED");
        p.setVerifiedBy(pharmacistUsername);
        p.setVerifiedAt(LocalDateTime.now());
        p.setStatus("CANCELLED");
        PharmacyPrescriptionRecord saved = prescriptionRepository.save(p);

        syncClinicalStatus(saved, "CANCELLED", pharmacistUsername);
        return saved;
    }

    @Transactional
    public PharmacyPrescriptionRecord dispensePrescription(Long id, String pharmacistUsername) {
        PharmacyPrescriptionRecord p = prescriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prescription not found"));

        if (!"VERIFIED".equals(p.getVerificationStatus())) {
            throw new IllegalStateException("Prescription must be verified before dispensing");
        }

        p.setStatus("DISPENSED");
        p.setDispensedAt(LocalDateTime.now());
        p.setDispensedBy(pharmacistUsername);
        PharmacyPrescriptionRecord saved = prescriptionRepository.save(p);

        // Sync dispensed status back to clinical record
        syncClinicalStatus(saved, "DISPENSED", pharmacistUsername);
        return saved;
    }

    // ── private helper ──────────────────────────────────────────────────────
    private void syncClinicalStatus(PharmacyPrescriptionRecord pharmRx, String newStatus, String performedBy) {
        if (pharmRx.getClinicalPrescriptionId() == null) return;
        clinicalPrescriptionRepository.findById(pharmRx.getClinicalPrescriptionId()).ifPresent(clinical -> {
            clinical.setPharmacyStatus(newStatus);
            if ("DISPENSED".equals(newStatus)) {
                clinical.setDispensedAt(pharmRx.getDispensedAt());
                clinical.setDispensedBy(performedBy);
            }
            clinicalPrescriptionRepository.save(clinical);
        });
    }
}
