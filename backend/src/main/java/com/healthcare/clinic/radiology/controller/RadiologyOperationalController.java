package com.healthcare.clinic.radiology.controller;

import com.healthcare.clinic.radiology.entity.RadiologyInventoryItem;
import com.healthcare.clinic.radiology.service.RadiologyOperationalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/radiology/operations")
@RequiredArgsConstructor
public class RadiologyOperationalController {

    private final RadiologyOperationalService operationalService;

    @PostMapping("/inventory/deduct")
    @PreAuthorize("hasAnyRole('TECHNICIAN', 'ADMIN')")
    public ResponseEntity<RadiologyInventoryItem> deductInventory(
            @RequestParam String sku,
            @RequestParam Long branchId,
            @RequestParam int quantity) {
        return ResponseEntity.ok(operationalService.deductInventory(sku, branchId, quantity));
    }

    @GetMapping("/inventory/low-stock")
    @PreAuthorize("hasAnyRole('TECHNICIAN', 'ADMIN', 'RADIOLOGIST')")
    public ResponseEntity<List<RadiologyInventoryItem>> getLowStockItems(@RequestParam Long branchId) {
        return ResponseEntity.ok(operationalService.getLowStockItems(branchId));
    }
}
