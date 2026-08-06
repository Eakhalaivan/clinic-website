package com.healthcare.clinic.pharmacy.controller;

import com.healthcare.clinic.inventory.entity.BaseEntity;
import com.healthcare.clinic.inventory.entity.Patient;
import jakarta.validation.Valid;

import com.healthcare.clinic.pharmacy.entity.PharmacyInsuranceClaim;
import com.healthcare.clinic.pharmacy.entity.InsuranceProvider;
import com.healthcare.clinic.common.dto.ApiResponse;
import com.healthcare.clinic.pharmacy.service.PharmacyInsuranceClaimService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Sort;
import com.healthcare.clinic.pharmacy.dto.common.PageResponse;
import java.util.List;

@RestController("pharmacyInsuranceClaimController")
@RequestMapping("/api/insurance-claims")
public class PharmacyInsuranceClaimController {

    private final PharmacyInsuranceClaimService service;

    public PharmacyInsuranceClaimController(PharmacyInsuranceClaimService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<PharmacyInsuranceClaim>>> getClaims(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(service.getAllClaims(pageable), "Claims fetched"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PharmacyInsuranceClaim>> getClaim(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success(service.getClaimById(id), "Claim details"));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_SYSTEM_ADMIN','ROLE_BILLING_STAFF','ROLE_CASHIER')")
    public ResponseEntity<ApiResponse<PharmacyInsuranceClaim>> create(@Valid @RequestBody PharmacyInsuranceClaim claim) {
        return ResponseEntity.ok(ApiResponse.success(service.createClaim(claim), "Claim draft created successfully"));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyAuthority('ROLE_SYSTEM_ADMIN','ROLE_ACCOUNTS_MANAGER','ROLE_SUPERVISOR')")
    public ResponseEntity<ApiResponse<PharmacyInsuranceClaim>> updateStatus(@PathVariable String id, @RequestParam String status) {
        return ResponseEntity.ok(ApiResponse.success(service.updateClaimStatus(id, status), "Claim status updated"));
    }

    @GetMapping("/providers")
    public ResponseEntity<ApiResponse<PageResponse<InsuranceProvider>>> getProviders(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(service.getAllProviders(pageable), "Insurance providers fetched"));
    }

    @PostMapping("/providers")
    @PreAuthorize("hasAnyAuthority('ROLE_SYSTEM_ADMIN','ROLE_SUPERVISOR')")
    public ResponseEntity<ApiResponse<InsuranceProvider>> createProvider(@Valid @RequestBody InsuranceProvider provider) {
        return ResponseEntity.ok(ApiResponse.success(service.createProvider(provider), "Insurance provider configured"));
    }
}
