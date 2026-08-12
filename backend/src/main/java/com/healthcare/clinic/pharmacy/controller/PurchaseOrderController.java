package com.healthcare.clinic.pharmacy.controller;

import jakarta.validation.Valid;

import com.healthcare.clinic.pharmacy.entity.PurchaseOrder;
import com.healthcare.clinic.common.dto.ApiResponse;
import com.healthcare.clinic.pharmacy.service.PurchaseOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Sort;
import com.healthcare.clinic.pharmacy.dto.common.PageResponse;

@RestController("pharmacyPurchaseOrderController")
@RequestMapping({"/api/purchase-orders", "/api/pharmacy/purchase-orders"})
public class PurchaseOrderController {

    private final PurchaseOrderService service;

    public PurchaseOrderController(PurchaseOrderService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<PurchaseOrder>>> getPOs(
            @RequestParam(value = "status",     required = false) String status,
            @RequestParam(value = "searchTerm", required = false) String searchTerm,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        // TopNav badge call: ?status=PENDING&size=20 — return Page so frontend counts totalElements
        if (status != null && !status.isBlank() && !status.equalsIgnoreCase("ALL")) {
            String dbStatus = switch (status.toUpperCase()) {
                case "PENDING"  -> "submitted";
                case "RECEIVED" -> "completed";
                default         -> status.toLowerCase();
            };
            java.util.List<String> valid = java.util.List.of(
                    "draft","submitted","approved","sent","partially_received","completed","cancelled");
            if (!valid.contains(dbStatus)) {
                return ResponseEntity.ok(ApiResponse.success(
                        new PageResponse<>(List.of(), 0, 0, 0, pageable.getPageSize()), "No POs found"));
            }
            PageResponse<PurchaseOrder> poPage =
                    service.getPOsByStatusPaged(dbStatus, pageable);
            return ResponseEntity.ok(ApiResponse.success(poPage, "POs fetched"));
        }

        // Normal list call with optional searchTerm
        PageResponse<PurchaseOrder> poPage =
                (searchTerm != null && !searchTerm.isBlank())
                        ? service.searchPOs(searchTerm.trim(), pageable)
                        : service.getAllPosPaged(pageable);
        return ResponseEntity.ok(ApiResponse.success(poPage, "POs fetched"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PurchaseOrder>> getPO(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success(service.getPoById(id), "PO details"));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_SYSTEM_ADMIN','ROLE_STOREKEEPER')")
    public ResponseEntity<ApiResponse<PurchaseOrder>> create(@Valid @RequestBody PurchaseOrder po) {
        return ResponseEntity.ok(ApiResponse.success(service.createPO(po), "PO created successfully"));
    }

    @PutMapping("/{id}/submit")
    @PreAuthorize("hasAnyAuthority('ROLE_SYSTEM_ADMIN','ROLE_STOREKEEPER')")
    public ResponseEntity<ApiResponse<PurchaseOrder>> submit(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success(service.submitForApproval(id), "PO submitted"));
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasAnyAuthority('ROLE_SYSTEM_ADMIN','ROLE_SUPERVISOR','ROLE_STOREKEEPER')")
    public ResponseEntity<ApiResponse<PurchaseOrder>> approve(@PathVariable String id, @RequestParam Long userId) {
        return ResponseEntity.ok(ApiResponse.success(service.approvePO(id, userId), "PO approved"));
    }

    @PutMapping("/{id}/send")
    @PreAuthorize("hasAnyAuthority('ROLE_SYSTEM_ADMIN')")
    public ResponseEntity<ApiResponse<PurchaseOrder>> send(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success(service.sendToSupplier(id), "PO sent to supplier"));
    }

    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasAnyAuthority('ROLE_SYSTEM_ADMIN','ROLE_PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<PurchaseOrder>> cancel(@PathVariable String id, @RequestParam String reason, @RequestParam Long userId) {
        return ResponseEntity.ok(ApiResponse.success(service.cancelPO(id, reason, userId), "PO cancelled"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_SYSTEM_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String id) {
        service.deletePO(id);
        return ResponseEntity.ok(ApiResponse.success(null, "PO deleted successfully"));
    }

    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getSummary() {
        return ResponseEntity.ok(ApiResponse.success(service.getPoSummary(), "PO summary stats"));
    }
}