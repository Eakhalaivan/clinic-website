package com.healthcare.clinic.fhir.controller;

import com.healthcare.clinic.fhir.service.FhirTransformService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/v1/fhir")
@RequiredArgsConstructor
public class FhirExportController {

    private final FhirTransformService fhirTransformService;

    @GetMapping("/Patient/{patientId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    public ResponseEntity<String> exportPatient(@PathVariable Long patientId) {
        String fhirJson = fhirTransformService.exportPatientAsFhirJson(patientId);
        return ResponseEntity.ok().header("Content-Type", "application/fhir+json").body(fhirJson);
    }
}
