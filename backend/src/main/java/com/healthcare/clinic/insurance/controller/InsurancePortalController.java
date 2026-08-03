package com.healthcare.clinic.insurance.controller;

import com.healthcare.clinic.finance.entity.InsuranceClaim;
import com.healthcare.clinic.insurance.entity.InsurancePreAuth;
import com.healthcare.clinic.insurance.service.InsurancePortalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/insurance")
@RequiredArgsConstructor
@PreAuthorize("hasRole('INSURANCE') or hasRole('SUPER_ADMIN') or hasRole('ACCOUNTANT')")
public class InsurancePortalController {

    private final InsurancePortalService insuranceService;

    @GetMapping("/claims")
    public ResponseEntity<List<InsuranceClaim>> getClaims() {
        return ResponseEntity.ok(insuranceService.getAllClaims());
    }

    @PatchMapping("/claims/{id}/adjudicate")
    public ResponseEntity<InsuranceClaim> adjudicateClaim(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam(required = false) BigDecimal approvedAmount,
            @RequestParam(required = false) String notes) {
        return ResponseEntity.ok(insuranceService.adjudicateClaim(id, status, approvedAmount, notes));
    }

    @GetMapping("/pre-auths")
    public ResponseEntity<List<InsurancePreAuth>> getPreAuths() {
        return ResponseEntity.ok(insuranceService.getAllPreAuths());
    }

    @PostMapping("/pre-auths")
    public ResponseEntity<InsurancePreAuth> submitPreAuth(@RequestBody InsurancePreAuth preAuth) {
        return ResponseEntity.ok(insuranceService.submitPreAuth(preAuth));
    }

    @PatchMapping("/pre-auths/{id}/adjudicate")
    public ResponseEntity<InsurancePreAuth> adjudicatePreAuth(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam(required = false) BigDecimal approvedAmount,
            @RequestParam(required = false) String denialReason) {
        return ResponseEntity.ok(insuranceService.adjudicatePreAuth(id, status, approvedAmount, denialReason));
    }
}
