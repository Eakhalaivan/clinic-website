package com.healthcare.clinic.pharmacy.service;

import com.healthcare.clinic.inventory.entity.BaseEntity;
import com.healthcare.clinic.inventory.entity.Patient;

import com.healthcare.clinic.pharmacy.entity.PharmacyInsuranceClaim;
import com.healthcare.clinic.pharmacy.entity.PharmacyInsuranceClaimLineItem;
import com.healthcare.clinic.pharmacy.entity.InsuranceProvider;
import com.healthcare.clinic.pharmacy.repository.PharmacyInsuranceClaimRepository;
import com.healthcare.clinic.pharmacy.repository.InsuranceProviderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.healthcare.clinic.pharmacy.dto.common.PageResponse;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service("pharmacyInsuranceClaimService")
public class PharmacyInsuranceClaimService {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(PharmacyInsuranceClaimService.class);

    private final PharmacyInsuranceClaimRepository claimRepository;
    private final InsuranceProviderRepository providerRepository;
    private final com.healthcare.clinic.pharmacy.repository.PharmacyUserRepository userRepository;

    public PharmacyInsuranceClaimService(PharmacyInsuranceClaimRepository claimRepository,
                                         InsuranceProviderRepository providerRepository,
                                         com.healthcare.clinic.pharmacy.repository.PharmacyUserRepository userRepository) {
        this.claimRepository = claimRepository;
        this.providerRepository = providerRepository;
        this.userRepository = userRepository;
    }

    public PageResponse<PharmacyInsuranceClaim> getAllClaims(Pageable pageable) {
        Page<PharmacyInsuranceClaim> pageResult = claimRepository.findAll(pageable);
        return new PageResponse<>(pageResult);
    }

    public PharmacyInsuranceClaim getClaimById(String id) {
        return claimRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Claim not found: " + id));
    }

    @Transactional
    public PharmacyInsuranceClaim createClaim(PharmacyInsuranceClaim claim) {
        claim.setClaimId(UUID.randomUUID().toString());
        claim.setClaimNumber("CLM-" + (System.currentTimeMillis() % 100000));
        claim.setClaimDate(LocalDate.now());
        claim.setClaimStatus("draft");
        claim.setCreatedBy(getCurrentUserId());

        if (claim.getCoveredAmount() == null) {
            java.math.BigDecimal total = claim.getTotalBillAmount() != null ? claim.getTotalBillAmount() : java.math.BigDecimal.ZERO;
            java.math.BigDecimal nonCovered = claim.getNonCoveredAmount() != null ? claim.getNonCoveredAmount() : java.math.BigDecimal.ZERO;
            claim.setCoveredAmount(total.subtract(nonCovered));
        }

        if (claim.getLineItems() != null) {
            claim.getLineItems().forEach(item -> {
                item.setClaimLineId(UUID.randomUUID().toString());
                item.setInsuranceClaim(claim);
            });
        }
        return claimRepository.save(claim);
    }

    private Long getCurrentUserId() {
        try {
            var auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated() && !(auth instanceof org.springframework.security.authentication.AnonymousAuthenticationToken)) {
                String username = auth.getName();
                return userRepository.findByUsername(username)
                        .map(com.healthcare.clinic.pharmacy.entity.PharmacyUser::getId)
                        .orElse(1L);
            }
        } catch (Exception e) {
            log.warn("Failed to retrieve current user ID from security context", e);
        }
        return 1L; // Fallback
    }

    @Transactional
    public PharmacyInsuranceClaim updateClaimStatus(String claimId, String status) {
        PharmacyInsuranceClaim claim = getClaimById(claimId);
        claim.setClaimStatus(status);
        if ("approved".equalsIgnoreCase(status)) {
            claim.setApprovalDate(LocalDate.now());
        } else if ("settled".equalsIgnoreCase(status)) {
            claim.setSettlementDate(LocalDate.now());
        }
        return claimRepository.save(claim);
    }

    public PageResponse<InsuranceProvider> getAllProviders(Pageable pageable) {
        Page<InsuranceProvider> pageResult = providerRepository.findAll(pageable);
        return new PageResponse<>(pageResult);
    }

    @Transactional
    public InsuranceProvider createProvider(InsuranceProvider provider) {
        provider.setProviderId(UUID.randomUUID().toString());
        return providerRepository.save(provider);
    }
}
