package com.healthcare.clinic.doctor.controller;

import com.healthcare.clinic.doctor.entity.ClinicalEncounter;
import com.healthcare.clinic.doctor.service.ClinicalEncounterService;
import com.healthcare.clinic.identity.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.healthcare.clinic.audit.annotation.AuditableAction;

import java.util.List;

@RestController
@RequestMapping("/api/v1/doctor/encounters")
@RequiredArgsConstructor
public class ClinicalEncounterController {

    private final ClinicalEncounterService encounterService;

    @GetMapping
    public ResponseEntity<List<ClinicalEncounter>> getMyEncounters(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(encounterService.getMyEncounters(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClinicalEncounter> getEncounter(@AuthenticationPrincipal User user, @PathVariable Long id) {
        return ResponseEntity.ok(encounterService.getEncounter(user, id));
    }

    @PostMapping
    @AuditableAction(module = "CLINICAL_ENCOUNTER", action = "OPEN", resourceType = "ClinicalEncounter", sensitivityLevel = "HIGH")
    public ResponseEntity<ClinicalEncounter> startEncounter(@AuthenticationPrincipal User user, @RequestBody ClinicalEncounter encounter) {
        return ResponseEntity.ok(encounterService.startEncounter(user, encounter));
    }

    @PostMapping("/{id}/close")
    @AuditableAction(module = "CLINICAL_ENCOUNTER", action = "CLOSE", resourceType = "ClinicalEncounter", sensitivityLevel = "HIGH")
    public ResponseEntity<ClinicalEncounter> closeEncounter(@AuthenticationPrincipal User user, @PathVariable Long id) {
        return ResponseEntity.ok(encounterService.closeEncounter(user, id));
    }
}
