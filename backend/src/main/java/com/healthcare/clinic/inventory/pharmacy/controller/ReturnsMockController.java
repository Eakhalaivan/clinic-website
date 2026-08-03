package com.healthcare.clinic.inventory.pharmacy.controller;

import com.healthcare.clinic.common.dto.ApiResponse;
import com.healthcare.clinic.inventory.sales.model.MedicineReturn;
import com.healthcare.clinic.inventory.sales.repository.MedicineReturnRepository;
import com.healthcare.clinic.inventory.entity.ReturnToSupplier;
import com.healthcare.clinic.inventory.pharmacy.repository.ReturnToSupplierRepository;
import com.healthcare.clinic.inventory.pharmacy.enums.ReturnStatus;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Wires all /api/returns/* endpoints to real repository queries.
 * Replaces the old ReturnsMockController that returned empty stubs.
 */
@RestController
@RequestMapping("/api/returns")
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_SUPER_ADMIN','ROLE_PHARMACIST')")
public class ReturnsMockController {

    private final MedicineReturnRepository medicineReturnRepository;
    private final ReturnToSupplierRepository returnToSupplierRepository;

    public ReturnsMockController(MedicineReturnRepository medicineReturnRepository,
                             ReturnToSupplierRepository returnToSupplierRepository) {
        this.medicineReturnRepository = medicineReturnRepository;
        this.returnToSupplierRepository = returnToSupplierRepository;
    }

    /** All customer return records (worklist view) */
    @GetMapping("/worklists")
    public ResponseEntity<ApiResponse<List<MedicineReturn>>> getWorklists() {
        List<MedicineReturn> all = medicineReturnRepository.findAll();
        return ResponseEntity.ok(ApiResponse.success(all, "Worklist retrieved"));
    }

    /** Customer returns awaiting approval (status = PENDING) */
    @GetMapping("/pending-replacements")
    public ResponseEntity<ApiResponse<List<MedicineReturn>>> getPendingReplacements() {
        // Use countByStatus to see if any pending, then fetch all and filter
        List<MedicineReturn> all = medicineReturnRepository.findAll();
        List<MedicineReturn> pending = all.stream()
            .filter(r -> ReturnStatus.PENDING.equals(r.getStatus()))
            .toList();
        return ResponseEntity.ok(ApiResponse.success(pending, "Pending replacements retrieved"));
    }

    /** Customer returns that were approved and credited */
    @GetMapping("/credit-returns")
    public ResponseEntity<ApiResponse<List<MedicineReturn>>> getCreditReturns() {
        List<MedicineReturn> all = medicineReturnRepository.findAll();
        List<MedicineReturn> approved = all.stream()
            .filter(r -> ReturnStatus.APPROVED.equals(r.getStatus()))
            .toList();
        return ResponseEntity.ok(ApiResponse.success(approved, "Credit returns retrieved"));
    }

    /** Customer returns that were rejected */
    @GetMapping("/direct-returns")
    public ResponseEntity<ApiResponse<List<MedicineReturn>>> getDirectReturns() {
        List<MedicineReturn> all = medicineReturnRepository.findAll();
        List<MedicineReturn> rejected = all.stream()
            .filter(r -> ReturnStatus.REJECTED.equals(r.getStatus()))
            .toList();
        return ResponseEntity.ok(ApiResponse.success(rejected, "Direct returns retrieved"));
    }

    /** Supplier returns (return-to-supplier records) */
    @GetMapping("/supplier-returns")
    public ResponseEntity<ApiResponse<List<ReturnToSupplier>>> getSupplierReturns() {
        List<ReturnToSupplier> all = returnToSupplierRepository.findAll();
        return ResponseEntity.ok(ApiResponse.success(all, "Supplier returns retrieved"));
    }
}
