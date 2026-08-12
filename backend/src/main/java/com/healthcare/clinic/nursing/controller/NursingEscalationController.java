package com.healthcare.clinic.nursing.controller;

import com.healthcare.clinic.nursing.dto.NurseEscalationRequest;
import com.healthcare.clinic.nursing.dto.NursingChecklistRequest;
import com.healthcare.clinic.nursing.entity.NurseEscalation;
import com.healthcare.clinic.nursing.entity.NursingChecklist;
import com.healthcare.clinic.nursing.service.NursingEscalationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/nursing/escalations")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('NURSE', 'CHARGE_NURSE')")
public class NursingEscalationController {

    private final NursingEscalationService escalationService;

    @PostMapping
    public ResponseEntity<NurseEscalation> createEscalation(@RequestBody NurseEscalationRequest request) {
        return ResponseEntity.ok(escalationService.createEscalation(request));
    }

    @PatchMapping("/{escalationId}/resolve")
    public ResponseEntity<NurseEscalation> resolveEscalation(
            @PathVariable Long escalationId,
            @RequestBody Map<String, String> payload) {
        return ResponseEntity.ok(escalationService.resolveEscalation(escalationId, payload.get("resolutionNotes")));
    }

    @GetMapping("/{patientId}")
    public ResponseEntity<List<NurseEscalation>> getPatientEscalations(@PathVariable Long patientId) {
        return ResponseEntity.ok(escalationService.getPatientEscalations(patientId));
    }

    @PostMapping("/checklists")
    public ResponseEntity<NursingChecklist> createChecklist(@RequestBody NursingChecklistRequest request) {
        return ResponseEntity.ok(escalationService.createChecklist(request));
    }

    @PatchMapping("/checklists/{checklistId}")
    public ResponseEntity<NursingChecklist> updateChecklist(
            @PathVariable Long checklistId,
            @RequestBody Map<String, String> payload) {
        return ResponseEntity.ok(escalationService.updateChecklistStatus(checklistId, payload.get("status"), payload.get("itemsJson")));
    }

    @GetMapping("/checklists/{patientId}")
    public ResponseEntity<List<NursingChecklist>> getPatientChecklists(@PathVariable Long patientId) {
        return ResponseEntity.ok(escalationService.getPatientChecklists(patientId));
    }
}
