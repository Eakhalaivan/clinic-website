package com.healthcare.clinic.doctor.service;

import com.healthcare.clinic.doctor.entity.ClinicalEncounter;
import com.healthcare.clinic.doctor.entity.DoctorProfile;
import com.healthcare.clinic.doctor.repository.ClinicalEncounterRepository;
import com.healthcare.clinic.doctor.repository.DoctorProfileRepository;
import com.healthcare.clinic.doctor.entity.Prescription;
import com.healthcare.clinic.identity.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClinicalEncounterService {

    private final ClinicalEncounterRepository encounterRepository;
    private final DoctorProfileRepository doctorProfileRepository;
    private final PrescriptionService prescriptionService;
    private final ClinicalBillingService billingService;

    private DoctorProfile getDoctorProfile(User user) {
        return doctorProfileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Doctor profile not found"));
    }

    public List<ClinicalEncounter> getMyEncounters(User user) {
        DoctorProfile doctor = getDoctorProfile(user);
        return encounterRepository.findByDoctorIdOrderByCreatedAtDesc(doctor.getId());
    }

    public ClinicalEncounter getEncounter(User user, Long id) {
        DoctorProfile doctor = getDoctorProfile(user);
        ClinicalEncounter encounter = encounterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Encounter not found"));
        
        if (!encounter.getDoctorId().equals(doctor.getId())) {
            throw new RuntimeException("Unauthorized to access this encounter");
        }
        
        return encounter;
    }

    @Transactional
    public ClinicalEncounter startEncounter(User user, ClinicalEncounter encounter) {
        DoctorProfile doctor = getDoctorProfile(user);
        encounter.setDoctorId(doctor.getId());
        encounter.setStatus("In Progress");
        return encounterRepository.save(encounter);
    }

    @Transactional
    public ClinicalEncounter finalizeEncounter(User user, Long id) {
        ClinicalEncounter encounter = getEncounter(user, id);
        if (encounter.getStatus().equals("Completed") || encounter.getStatus().equals("Finalized")) {
            throw new RuntimeException("Encounter is already finalized or completed");
        }
        
        // Finalize all draft prescriptions for this encounter
        List<Prescription> prescriptions = prescriptionService.getPrescriptionsByEncounter(id);
        for (Prescription p : prescriptions) {
            if ("Draft".equals(p.getStatus())) {
                prescriptionService.signPrescription(p.getId());
            }
        }

        // Create billing outbox entry for consultation
        billingService.createBillingEvent(
                encounter.getId(),
                encounter.getPatientId(),
                encounter.getDoctorId(),
                "Consultation",
                "CONS-01",
                new BigDecimal("150.00") // Example base fee
        );

        encounter.setStatus("Completed");
        encounter.setFinalizedAt(ZonedDateTime.now());
        return encounterRepository.save(encounter);
    }
}
