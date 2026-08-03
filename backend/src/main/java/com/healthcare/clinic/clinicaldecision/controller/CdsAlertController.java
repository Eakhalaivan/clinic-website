package com.healthcare.clinic.clinicaldecision.controller;

import com.healthcare.clinic.common.dto.ApiResponse;
import com.healthcare.clinic.clinicaldecision.entity.CdsAlert;
import com.healthcare.clinic.clinicaldecision.service.CdsAlertService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cds/alerts")
@RequiredArgsConstructor
public class CdsAlertController {

    private final CdsAlertService alertService;

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('DOCTOR', 'NURSE', 'ADMIN', 'SUPER_ADMIN') or @securityUtils.isSameUser(#patientId)")
    public ResponseEntity<ApiResponse<List<CdsAlert>>> getAlertsForPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(ApiResponse.success(alertService.getAlertsForPatient(patientId)));
    }

    @GetMapping("/pending")
    @PreAuthorize("hasAnyRole('DOCTOR', 'NURSE', 'ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<CdsAlert>>> getPendingAlerts() {
        return ResponseEntity.ok(ApiResponse.success(alertService.getPendingAlerts()));
    }

    @PostMapping("/{id}/acknowledge")
    @PreAuthorize("hasAnyRole('DOCTOR', 'NURSE', 'ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<CdsAlert>> acknowledgeAlert(
            @PathVariable Long id,
            @RequestBody(required = false) AcknowledgeRequest request) {
        String reason = request != null ? request.getOverrideReason() : null;
        return ResponseEntity.ok(ApiResponse.success(alertService.acknowledgeAlert(id, reason), "CDS alert acknowledged"));
    }
}

@Data
class AcknowledgeRequest {
    private String overrideReason;
}
