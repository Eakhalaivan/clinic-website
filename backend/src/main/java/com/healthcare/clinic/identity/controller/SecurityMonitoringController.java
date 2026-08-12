package com.healthcare.clinic.identity.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/audits/security-monitoring")
public class SecurityMonitoringController {

    @GetMapping("/alerts")
    public ResponseEntity<Map<String, Object>> getSecurityAlerts() {
        return ResponseEntity.ok(Map.of("status", "SECURE", "alerts", 0));
    }

    @PostMapping("/revoke-session/{sessionId}")
    public ResponseEntity<Void> revokeSession(@PathVariable String sessionId) {
        return ResponseEntity.ok().build();
    }
}
