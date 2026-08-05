package com.healthcare.clinic.insurance.controller;

import com.healthcare.clinic.insurance.entity.InsurancePreAuth;
import com.healthcare.clinic.insurance.repository.InsurancePreAuthRepository;
import com.healthcare.clinic.patient.entity.PatientProfile;
import com.healthcare.clinic.patient.repository.PatientProfileRepository;
import com.healthcare.clinic.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/patient/insurance")
@RequiredArgsConstructor
public class PatientInsuranceController {

    private final InsurancePreAuthRepository insurancePreAuthRepository;
    private final PatientProfileRepository patientProfileRepository;

    @GetMapping("/pre-auths")
    @PreAuthorize("hasAuthority('ROLE_PATIENT')")
    public ResponseEntity<List<InsurancePreAuth>> getMyPreAuths() {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        
        Optional<PatientProfile> patientOpt = patientProfileRepository.findByUserId(currentUserId);
        if (patientOpt.isEmpty()) {
            return ResponseEntity.ok(List.of());
        }
        
        Long patientId = patientOpt.get().getId();
        
        List<InsurancePreAuth> preAuths = insurancePreAuthRepository.findByPatientId(patientId);
        
        return ResponseEntity.ok(preAuths);
    }
}
