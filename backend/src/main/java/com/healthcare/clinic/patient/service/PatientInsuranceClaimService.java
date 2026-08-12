package com.healthcare.clinic.patient.service;

import com.healthcare.clinic.identity.entity.User;
import com.healthcare.clinic.patient.entity.PatientInsuranceClaim;
import com.healthcare.clinic.patient.entity.PatientProfile;
import com.healthcare.clinic.patient.repository.PatientInsuranceClaimRepository;
import com.healthcare.clinic.patient.repository.PatientProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientInsuranceClaimService {

    private final PatientInsuranceClaimRepository claimRepository;
    private final PatientProfileRepository patientProfileRepository;

    private PatientProfile getPatientProfile(User user) {
        return patientProfileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Patient profile not found for user"));
    }

    public List<PatientInsuranceClaim> getClaims(User user) {
        PatientProfile profile = getPatientProfile(user);
        return claimRepository.findByPatientIdOrderBySubmittedAtDesc(profile.getId());
    }

    @Transactional
    public PatientInsuranceClaim submitClaim(User user, PatientInsuranceClaim claim) {
        PatientProfile profile = getPatientProfile(user);
        claim.setPatientId(profile.getId());
        claim.setStatus("Submitted");
        return claimRepository.save(claim);
    }
}
