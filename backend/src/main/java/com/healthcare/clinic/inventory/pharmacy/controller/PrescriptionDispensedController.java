package com.healthcare.clinic.inventory.pharmacy.controller;

import com.healthcare.clinic.identity.entity.User;
import com.healthcare.clinic.inventory.pharmacy.entity.PrescriptionDispensed;
import com.healthcare.clinic.inventory.pharmacy.repository.PrescriptionDispensedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/pharmacy/dispensed")
@RequiredArgsConstructor
@PreAuthorize("hasRole('PHARMACIST') or hasRole('SUPER_ADMIN')")
public class PrescriptionDispensedController {

    private final PrescriptionDispensedRepository dispensedRepository;

    @GetMapping
    public ResponseEntity<List<PrescriptionDispensed>> getMyDispensedPrescriptions(@AuthenticationPrincipal User pharmacist) {
        return ResponseEntity.ok(dispensedRepository.findByPharmacistId(pharmacist.getId()));
    }

    @PostMapping
    public ResponseEntity<PrescriptionDispensed> dispensePrescription(@RequestBody PrescriptionDispensed dispensed, @AuthenticationPrincipal User pharmacist) {
        dispensed.setPharmacist(pharmacist);
        dispensed.setDispensedAt(ZonedDateTime.now());
        // In a real system, you would loop through items, reduce inventory from batches, and save items
        return ResponseEntity.ok(dispensedRepository.save(dispensed));
    }
}
