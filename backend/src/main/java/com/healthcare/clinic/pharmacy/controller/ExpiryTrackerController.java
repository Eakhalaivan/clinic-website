package com.healthcare.clinic.pharmacy.controller;

import com.healthcare.clinic.inventory.entity.BaseEntity;
import com.healthcare.clinic.inventory.entity.Patient;
import jakarta.validation.Valid;

import com.healthcare.clinic.pharmacy.entity.BatchReturnToSupplier;
import com.healthcare.clinic.pharmacy.entity.StockBatch;
import com.healthcare.clinic.common.dto.ApiResponse;
import com.healthcare.clinic.pharmacy.service.ExpiryTrackerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import com.healthcare.clinic.pharmacy.dto.common.PageResponse;
import java.util.List;
import java.util.Map;

@RestController("pharmacyExpiryTrackerController")
@RequestMapping("/api/pharmacy/expiry-tracker")
public class ExpiryTrackerController {

    private final ExpiryTrackerService service;

    public ExpiryTrackerController(ExpiryTrackerService service) {
        this.service = service;
    }

    @GetMapping("/batches")
    public ResponseEntity<ApiResponse<PageResponse<StockBatch>>> getBatches(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "expiryDate,asc") String[] sort) {
        Sort.Direction dir = sort[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(dir, sort[0]));
        return ResponseEntity.ok(ApiResponse.success(service.getFefoStockView(pageRequest), "Batches fetched successfully"));
    }

    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getSummary() {
        return ResponseEntity.ok(ApiResponse.success(service.getExpirySummary(), "Expiry summary fetched"));
    }

    @PostMapping("/return")
    @PreAuthorize("hasAnyAuthority('ROLE_SYSTEM_ADMIN','ROLE_PHARMACY_STAFF','ROLE_STOREKEEPER')")
    public ResponseEntity<ApiResponse<BatchReturnToSupplier>> initiateReturn(@Valid @RequestBody BatchReturnToSupplier returnRequest) {
        return ResponseEntity.ok(ApiResponse.success(service.initiateBatchReturn(returnRequest), "Return initiated successfully"));
    }

    @PutMapping("/return/{returnId}/status")
    @PreAuthorize("hasAnyAuthority('ROLE_SYSTEM_ADMIN','ROLE_STOREKEEPER')")
    public ResponseEntity<ApiResponse<BatchReturnToSupplier>> updateStatus(@PathVariable String returnId, @RequestParam String status) {
        return ResponseEntity.ok(ApiResponse.success(service.updateReturnStatus(returnId, status), "Status updated"));
    }

    @GetMapping("/returns")
    public ResponseEntity<ApiResponse<PageResponse<BatchReturnToSupplier>>> getReturns(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt,desc") String[] sort) {
        Sort.Direction dir = sort[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(dir, sort[0]));
        return ResponseEntity.ok(ApiResponse.success(service.getAllReturns(pageRequest), "Return records fetched"));
    }
}