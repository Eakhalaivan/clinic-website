package com.healthcare.clinic.pharmacy.controller;

import com.healthcare.clinic.inventory.entity.BaseEntity;
import com.healthcare.clinic.inventory.entity.Patient;

import com.healthcare.clinic.common.dto.ApiResponse;
import com.healthcare.clinic.pharmacy.entity.PharmacyPrescriptionRecord;
import com.healthcare.clinic.pharmacy.repository.PrescriptionRepository;
import com.healthcare.clinic.pharmacy.service.PrescriptionVerificationService;
import com.healthcare.clinic.security.SecurityUtils;
import com.healthcare.clinic.identity.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController("pharmacyPrescriptionController")
@RequestMapping("/api/pharmacy/prescriptions")
public class PrescriptionController {

    private final PrescriptionRepository prescriptionRepository;
    private final PrescriptionVerificationService verificationService;
    private final UserRepository userRepository;

    public PrescriptionController(
            PrescriptionRepository prescriptionRepository,
            PrescriptionVerificationService verificationService,
            UserRepository userRepository) {
        this.prescriptionRepository = prescriptionRepository;
        this.verificationService = verificationService;
        this.userRepository = userRepository;
    }

    /** Returns all PENDING prescriptions with medication items */
    @GetMapping("/pending")
    @PreAuthorize("hasAnyAuthority('ROLE_PHARMACIST','ROLE_DOCTOR','ROLE_ADMIN','ROLE_SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<PharmacyPrescriptionRecord>>> getPendingPrescriptions() {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        List<PharmacyPrescriptionRecord> pending = prescriptionRepository.findAll().stream()
                .filter(p -> "PENDING".equals(p.getStatus()))
                .filter(p -> p.getAssignedPharmacyUserId() == null || p.getAssignedPharmacyUserId().equals(currentUserId))
                .toList();
        return ResponseEntity.ok(ApiResponse.success(pending, "Pending prescriptions fetched"));
    }

    /** Returns all prescriptions (for pharmacy dashboard) */
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_PHARMACIST','ROLE_ADMIN','ROLE_SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<PharmacyPrescriptionRecord>>> getAllPrescriptions() {
        return ResponseEntity.ok(ApiResponse.success(prescriptionRepository.findAll(), "All prescriptions fetched"));
    }

    /** Verify a prescription (pharmacist check) */
    @PostMapping("/{id}/verify")
    @PreAuthorize("hasAnyAuthority('ROLE_PHARMACIST','ROLE_ADMIN','ROLE_SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<PharmacyPrescriptionRecord>> verify(@PathVariable Long id) {
        String pharmacist = getCurrentPharmacistName();
        return ResponseEntity.ok(ApiResponse.success(
                verificationService.verifyPrescription(id, pharmacist), "Prescription verified"));
    }

    /** Reject a prescription */
    @PostMapping("/{id}/reject")
    @PreAuthorize("hasAnyAuthority('ROLE_PHARMACIST','ROLE_ADMIN','ROLE_SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<PharmacyPrescriptionRecord>> reject(
            @PathVariable Long id,
            @RequestBody(required = false) Map<String, String> body) {
        String reason = body != null ? body.getOrDefault("reason", "") : "";
        String pharmacist = getCurrentPharmacistName();
        return ResponseEntity.ok(ApiResponse.success(
                verificationService.rejectPrescription(id, reason, pharmacist), "Prescription rejected"));
    }

    /** Dispense a prescription — marks it DISPENSED and syncs back to clinical record */
    @PostMapping("/{id}/dispense")
    @PreAuthorize("hasAnyAuthority('ROLE_PHARMACIST','ROLE_ADMIN','ROLE_SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<PharmacyPrescriptionRecord>> dispense(@PathVariable Long id) {
        String pharmacist = getCurrentPharmacistName();
        return ResponseEntity.ok(ApiResponse.success(
                verificationService.dispensePrescription(id, pharmacist), "Prescription dispensed"));
    }

    // ── helpers ───────────────────────────────────────────────────────────────
    private String getCurrentPharmacistName() {
        try {
            Long userId = SecurityUtils.getCurrentUserId();
            return userRepository.findById(userId)
                    .map(u -> u.getFirstName() + " " + u.getLastName())
                    .orElse("Pharmacist");
        } catch (Exception e) {
            return "Pharmacist";
        }
    }
}
