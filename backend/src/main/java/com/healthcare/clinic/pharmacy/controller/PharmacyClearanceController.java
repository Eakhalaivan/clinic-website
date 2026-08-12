package com.healthcare.clinic.pharmacy.controller;


import com.healthcare.clinic.pharmacy.entity.PharmacyClearance;
import com.healthcare.clinic.pharmacy.service.PharmacyClearanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pharmacy/clearances")
@RequiredArgsConstructor
public class PharmacyClearanceController {

    private final PharmacyClearanceService pharmacyClearanceService;

    @GetMapping
    @PreAuthorize("hasAnyRole('PHARMACIST', 'ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<List<PharmacyClearance>> getAllClearances() {
        return ResponseEntity.ok(pharmacyClearanceService.getAllClearances());
    }

    @PostMapping("/{id}/clear")
    @PreAuthorize("hasAnyRole('PHARMACIST', 'ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<PharmacyClearance> markAsCleared(
            @PathVariable Long id, 
            Authentication authentication) {
        return ResponseEntity.ok(pharmacyClearanceService.markAsCleared(id, authentication.getName()));
    }
}
