package com.healthcare.clinic.audit.controller;

import com.healthcare.clinic.audit.entity.AuditRecord;
import com.healthcare.clinic.audit.repository.AuditRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/audit")
@RequiredArgsConstructor
public class AuditDashboardController {

    private final AuditRecordRepository auditRecordRepository;

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN', 'SUPER_ADMIN', 'COMPLIANCE_OFFICER')")
    public ResponseEntity<Page<AuditRecord>> searchAuditLogs(
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) Long actorId,
            @RequestParam(required = false) String moduleName,
            @RequestParam(required = false) String actionName,
            @RequestParam(required = false) String outcome,
            @RequestParam(required = false) Long tenantId,
            @PageableDefault(size = 50, sort = "createdAt", direction = org.springframework.data.domain.Sort.Direction.DESC) Pageable pageable) {
        
        Page<AuditRecord> results = auditRecordRepository.searchAuditLogs(
                patientId, actorId, moduleName, actionName, outcome, tenantId, pageable);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('DOCTOR', 'NURSE', 'SYSTEM_ADMIN', 'SUPER_ADMIN', 'COMPLIANCE_OFFICER')")
    public ResponseEntity<Page<AuditRecord>> getPatientAuditTimeline(
            @PathVariable Long patientId,
            @PageableDefault(size = 20, sort = "createdAt", direction = org.springframework.data.domain.Sort.Direction.DESC) Pageable pageable) {
        
        Page<AuditRecord> results = auditRecordRepository.findByPatientIdOrderByCreatedAtDesc(patientId, pageable);
        return ResponseEntity.ok(results);
    }
}
