package com.healthcare.clinic.insurance.service;

import com.healthcare.clinic.finance.entity.InsuranceClaim;
import com.healthcare.clinic.finance.repository.InsuranceClaimRepository;
import com.healthcare.clinic.insurance.entity.InsurancePreAuth;
import com.healthcare.clinic.insurance.repository.InsurancePreAuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InsurancePortalService {

    private final InsuranceClaimRepository claimRepository;
    private final InsurancePreAuthRepository preAuthRepository;

    @Transactional(readOnly = true)
    public List<InsuranceClaim> getAllClaims() {
        return claimRepository.findAllByOrderBySubmittedAtDesc();
    }

    @Transactional
    public InsuranceClaim adjudicateClaim(Long claimId, String status, BigDecimal approvedAmount, String notes) {
        InsuranceClaim claim = claimRepository.findById(claimId).orElseThrow();
        claim.setStatus(status);
        if (approvedAmount != null) {
            claim.setApprovedAmount(approvedAmount);
        }
        if (notes != null && !notes.isEmpty()) {
            claim.setNotes(notes);
        }
        if ("APPROVED".equals(status) || "SETTLED".equals(status) || "REJECTED".equals(status)) {
            claim.setSettledAt(ZonedDateTime.now());
        }
        return claimRepository.save(claim);
    }

    @Transactional(readOnly = true)
    public List<InsurancePreAuth> getAllPreAuths() {
        return preAuthRepository.findAllByOrderBySubmittedAtDesc();
    }

    @Transactional
    public InsurancePreAuth submitPreAuth(InsurancePreAuth preAuth) {
        return preAuthRepository.save(preAuth);
    }

    @Transactional
    public InsurancePreAuth adjudicatePreAuth(Long preAuthId, String status, BigDecimal approvedAmount, String denialReason) {
        InsurancePreAuth preAuth = preAuthRepository.findById(preAuthId).orElseThrow();
        preAuth.setStatus(status);
        if (approvedAmount != null) {
            preAuth.setApprovedAmount(approvedAmount);
        }
        if (denialReason != null && !denialReason.isEmpty()) {
            preAuth.setDenialReason(denialReason);
        }
        preAuth.setAdjudicatedAt(ZonedDateTime.now());
        return preAuthRepository.save(preAuth);
    }
}
