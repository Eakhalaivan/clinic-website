package com.healthcare.clinic.pharmacy.service;


import com.healthcare.clinic.pharmacy.entity.PharmacyPrescriptionItem;
import com.healthcare.clinic.pharmacy.entity.PharmacyPrescriptionRecord;
import com.healthcare.clinic.pharmacy.repository.PrescriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PharmacyPrescriptionSyncService {

    private final PrescriptionRepository pharmacyPrescriptionRepository;

    @Transactional(transactionManager = "pharmacyTransactionManager")
    public void syncNewPrescription(String patientName, String doctorName, Long clinicalPrescriptionId, 
                                    List<PharmacyPrescriptionItem> items) {
        PharmacyPrescriptionRecord pharmRx = new PharmacyPrescriptionRecord();
        pharmRx.setPatientName(patientName);
        pharmRx.setDoctorName(doctorName);
        pharmRx.setPrescriptionDate(LocalDateTime.now());
        pharmRx.setStatus("PENDING");
        pharmRx.setVerificationStatus("UNVERIFIED");
        pharmRx.setClinicalPrescriptionId(clinicalPrescriptionId);

        items.forEach(pharmRx::addItem);
        pharmacyPrescriptionRepository.save(pharmRx);
    }

    @Transactional(transactionManager = "pharmacyTransactionManager")
    public void syncSendPrescription(String patientName, String doctorName, Long clinicalPrescriptionId, 
                                     Long pharmacyUserId, List<PharmacyPrescriptionItem> items) {
        PharmacyPrescriptionRecord pharmRx = new PharmacyPrescriptionRecord();
        pharmRx.setPatientName(patientName);
        pharmRx.setDoctorName(doctorName);
        pharmRx.setPrescriptionDate(LocalDateTime.now());
        pharmRx.setStatus("PENDING");
        pharmRx.setVerificationStatus("UNVERIFIED");
        pharmRx.setClinicalPrescriptionId(clinicalPrescriptionId);
        if (pharmacyUserId != null) {
            pharmRx.setAssignedPharmacyUserId(pharmacyUserId);
        }

        items.forEach(pharmRx::addItem);
        pharmacyPrescriptionRepository.save(pharmRx);
    }

    @Transactional(transactionManager = "pharmacyTransactionManager")
    public void syncVoidPrescription(Long clinicalPrescriptionId) {
        pharmacyPrescriptionRepository.findAll().stream()
                .filter(p -> clinicalPrescriptionId.equals(p.getClinicalPrescriptionId()))
                .forEach(p -> {
                    p.setStatus("CANCELLED");
                    pharmacyPrescriptionRepository.save(p);
                });
    }
}
