package com.healthcare.clinic.doctor.controller;

import com.healthcare.clinic.doctor.entity.TeleconsultationSession;
import com.healthcare.clinic.doctor.service.TeleconsultationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController("sharedTeleconsultationController")
@RequestMapping("/api/v1/teleconsultations")
@RequiredArgsConstructor
public class TeleconsultationController {

    private final TeleconsultationService teleconsultationService;

    @GetMapping("/encounter/{encounterId}")
    @PreAuthorize("hasAnyRole('DOCTOR', 'NURSE', 'PATIENT')")
    public ResponseEntity<TeleconsultationSession> getSessionForEncounter(@PathVariable Long encounterId) {
        return ResponseEntity.ok(teleconsultationService.getOrCreateSessionForEncounter(encounterId));
    }

    @PutMapping("/{sessionId}/status")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<TeleconsultationSession> updateStatus(
            @PathVariable Long sessionId,
            @RequestParam String status) {
        return ResponseEntity.ok(teleconsultationService.updateStatus(sessionId, status));
    }
}
