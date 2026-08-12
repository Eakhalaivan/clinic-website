package com.healthcare.clinic.reception.service;

import com.healthcare.clinic.reception.entity.InsuranceVerification;
import com.healthcare.clinic.reception.repository.InsuranceVerificationRepository;
import com.healthcare.clinic.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InsuranceVerificationService {

    private final InsuranceVerificationRepository repository;

    @Transactional
    public InsuranceVerification requestVerification(Long patientId, String provider, String policyNumber) {
        InsuranceVerification verification = InsuranceVerification.builder()
                .patientId(patientId)
                .insuranceProvider(provider)
                .policyNumber(policyNumber)
                .status("PENDING")
                .build();
        return repository.save(verification);
    }

    @Transactional
    public InsuranceVerification verifyInsurance(Long verificationId, String status, String coverageDetails) {
        InsuranceVerification verification = repository.findById(verificationId)
                .orElseThrow(() -> new RuntimeException("Insurance Verification not found"));
        
        verification.setStatus(status);
        verification.setCoverageDetails(coverageDetails);
        verification.setVerifiedAt(LocalDateTime.now());
        verification.setVerifiedBy(SecurityUtils.getCurrentUserId());
        
        return repository.save(verification);
    }

    public List<InsuranceVerification> getPatientVerifications(Long patientId) {
        return repository.findByPatientId(patientId);
    }
}
