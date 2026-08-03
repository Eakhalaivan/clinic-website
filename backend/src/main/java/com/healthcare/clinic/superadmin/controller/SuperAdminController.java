package com.healthcare.clinic.superadmin.controller;

import com.healthcare.clinic.superadmin.entity.SuperAdminAuditLog;
import com.healthcare.clinic.superadmin.entity.SubscriptionPlan;
import com.healthcare.clinic.superadmin.entity.SystemConfiguration;
import com.healthcare.clinic.superadmin.service.SuperAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/super-admin")
@RequiredArgsConstructor
@PreAuthorize("hasAnyAuthority('ROLE_SUPER_ADMIN', 'ROLE_ADMIN')")
public class SuperAdminController {

    private final SuperAdminService superAdminService;

    // Platform stats
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        return ResponseEntity.ok(superAdminService.getPlatformStats());
    }

    // System Configuration
    @GetMapping("/configs")
    public ResponseEntity<List<SystemConfiguration>> getConfigs() {
        return ResponseEntity.ok(superAdminService.getAllConfigs());
    }

    @PutMapping("/configs/{id}")
    public ResponseEntity<SystemConfiguration> updateConfig(
            @PathVariable Long id,
            @RequestParam String value,
            @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(superAdminService.updateConfig(id, value, user.getUsername()));
    }

    // Subscription Plans
    @GetMapping("/subscription-plans")
    public ResponseEntity<List<SubscriptionPlan>> getPlans() {
        return ResponseEntity.ok(superAdminService.getAllPlans());
    }

    @PostMapping("/subscription-plans")
    public ResponseEntity<SubscriptionPlan> createPlan(@RequestBody SubscriptionPlan plan) {
        return ResponseEntity.ok(superAdminService.createPlan(plan));
    }

    @PatchMapping("/subscription-plans/{id}/toggle")
    public ResponseEntity<SubscriptionPlan> togglePlan(@PathVariable Long id) {
        return ResponseEntity.ok(superAdminService.togglePlanStatus(id));
    }

    // Audit Logs
    @GetMapping("/audit-logs")
    public ResponseEntity<Page<SuperAdminAuditLog>> getAuditLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        return ResponseEntity.ok(superAdminService.getAuditLogs(page, size));
    }
}
