package com.healthcare.clinic.reception.service;

import com.healthcare.clinic.reception.dto.IdentityVerificationRequest;
import com.healthcare.clinic.reception.dto.IdentityVerificationResponse;
import com.healthcare.clinic.reception.entity.PatientIdentityVerification;
import com.healthcare.clinic.reception.repository.PatientIdentityVerificationRepository;
import com.healthcare.clinic.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IdentityVerificationService {

    private final PatientIdentityVerificationRepository verificationRepository;

    @Transactional
    public IdentityVerificationResponse verifyIdentity(IdentityVerificationRequest request) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        
        PatientIdentityVerification verification = PatientIdentityVerification.builder()
                .patientId(request.getPatientId())
                .verificationMethod(request.getVerificationMethod())
                .verifiedByUserId(currentUserId)
                .status("SUCCESS") // Dummy for now, actual implementation would integrate with OTP/Document service
                .documentReference(request.getDocumentReference())
                .build();
                
        verification = verificationRepository.save(verification);
        return mapToResponse(verification);
    }

    public List<IdentityVerificationResponse> getVerificationsForPatient(Long patientId) {
        return verificationRepository.findByPatientIdOrderByVerifiedAtDesc(patientId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private IdentityVerificationResponse mapToResponse(PatientIdentityVerification entity) {
        return IdentityVerificationResponse.builder()
                .id(entity.getId())
                .patientId(entity.getPatientId())
                .verificationMethod(entity.getVerificationMethod())
                .verifiedByUserId(entity.getVerifiedByUserId())
                .verifiedAt(entity.getVerifiedAt())
                .status(entity.getStatus())
                .failureReason(entity.getFailureReason())
                .documentReference(entity.getDocumentReference())
                .build();
    }
}
