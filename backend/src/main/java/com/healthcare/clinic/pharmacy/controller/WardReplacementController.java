package com.healthcare.clinic.pharmacy.controller;

import com.healthcare.clinic.common.dto.ApiResponse;
import com.healthcare.clinic.pharmacy.entity.WardReplacementRequest;
import com.healthcare.clinic.pharmacy.service.WardReplacementService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("pharmacyWardReplacementController")
@RequestMapping("/api/pharmacy/ward-replacements")
public class WardReplacementController {

    private final WardReplacementService service;

    public WardReplacementController(WardReplacementService service) {
        this.service = service;
    }

    @GetMapping("/pending")
    public ResponseEntity<ApiResponse<List<WardReplacementRequest>>> getPending() {
        return ResponseEntity.ok(ApiResponse.success(service.getPendingReplacements(), "Pending replacement requests fetched"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<WardReplacementRequest>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(service.getAllReplacements(), "All replacement requests fetched"));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_SYSTEM_ADMIN','ROLE_PHARMACIST','ROLE_SUPERVISOR')")
    @PostMapping
    public ResponseEntity<ApiResponse<WardReplacementRequest>> create(@RequestBody CreateRequest body) {
        WardReplacementRequest created = service.createRequest(body.ward(), body.requestedBy(), body.items());
        return ResponseEntity.ok(ApiResponse.success(created, "Replacement request submitted"));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_SYSTEM_ADMIN','ROLE_PHARMACIST','ROLE_SUPERVISOR')")
    @PostMapping("/{id}/approve")
    public ResponseEntity<ApiResponse<Void>> approve(@PathVariable Long id) {
        service.approve(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Replacement request approved and sent to ward"));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_SYSTEM_ADMIN','ROLE_PHARMACIST','ROLE_SUPERVISOR')")
    @PostMapping("/{id}/reject")
    public ResponseEntity<ApiResponse<Void>> reject(@PathVariable Long id) {
        service.reject(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Replacement request rejected"));
    }

    public record CreateRequest(String ward, String requestedBy, List<WardReplacementService.ReplacementItemRequest> items) {}
}
