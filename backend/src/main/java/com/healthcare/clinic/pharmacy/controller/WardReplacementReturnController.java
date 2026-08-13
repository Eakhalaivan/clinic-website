package com.healthcare.clinic.pharmacy.controller;

import com.healthcare.clinic.common.dto.ApiResponse;
import com.healthcare.clinic.pharmacy.entity.WardReplacementReturn;
import com.healthcare.clinic.pharmacy.service.WardReplacementReturnService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("pharmacyWardReplacementReturnController")
@RequestMapping("/api/pharmacy/ward-replacement-returns")
public class WardReplacementReturnController {

    private final WardReplacementReturnService service;

    public WardReplacementReturnController(WardReplacementReturnService service) {
        this.service = service;
    }

    @GetMapping("/pending")
    public ResponseEntity<ApiResponse<List<WardReplacementReturn>>> getPending() {
        return ResponseEntity.ok(ApiResponse.success(service.getPendingReturns(), "Pending return requests fetched"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<WardReplacementReturn>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(service.getAllReturns(), "All return requests fetched"));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_SYSTEM_ADMIN','ROLE_PHARMACY_STAFF','ROLE_SUPERVISOR')")
    @PostMapping("/{id}/approve")
    public ResponseEntity<ApiResponse<Void>> approve(@PathVariable Long id) {
        service.approve(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Return request approved"));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_SYSTEM_ADMIN','ROLE_PHARMACY_STAFF','ROLE_SUPERVISOR')")
    @PostMapping("/{id}/reject")
    public ResponseEntity<ApiResponse<Void>> reject(@PathVariable Long id) {
        service.reject(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Return request rejected"));
    }
}
