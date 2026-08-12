package com.healthcare.clinic.superadmin.controller;

import com.healthcare.clinic.superadmin.entity.FeatureFlag;
import com.healthcare.clinic.superadmin.service.FeatureFlagService;
import com.healthcare.clinic.superadmin.service.IntegrationService;
import com.healthcare.clinic.superadmin.service.SessionManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/super-admin/portal")
@RequiredArgsConstructor
@PreAuthorize("hasRole('SUPER_ADMIN')")
public class SuperAdminPortalController {
    
    private final FeatureFlagService featureFlagService;
    private final IntegrationService integrationService;
    private final SessionManagementService sessionManagementService;

    @GetMapping("/feature-flags")
    public ResponseEntity<List<FeatureFlag>> getFeatureFlags() {
        return ResponseEntity.ok(featureFlagService.getAllFlags());
    }

    @PostMapping("/feature-flags")
    public ResponseEntity<FeatureFlag> createFeatureFlag(@RequestBody FeatureFlag flag) {
        return ResponseEntity.ok(featureFlagService.createOrUpdateFlag(flag));
    }
    
    @GetMapping("/sessions")
    public ResponseEntity<?> getSessions() {
        return ResponseEntity.ok(sessionManagementService.getActiveSessions());
    }
    
    @PostMapping("/sessions/{id}/revoke")
    public ResponseEntity<?> revokeSession(@PathVariable Long id) {
        sessionManagementService.revokeSession(id);
        return ResponseEntity.ok().build();
    }
}
