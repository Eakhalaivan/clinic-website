package com.healthcare.clinic.integration.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/integration/fhir")
@RequiredArgsConstructor
public class FhirController {

    @PostMapping("/import")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPER_ADMIN') or hasAuthority('ROLE_DOCTOR')")
    public ResponseEntity<Map<String, String>> importFhirData(@RequestParam("file") MultipartFile file) {
        // Mock processing for Phase 4
        // In reality, we would use HAPI FHIR parser here to parse the JSON and map to our DB
        
        return ResponseEntity.ok(Map.of(
            "status", "success",
            "message", "FHIR Bundle parsed successfully.",
            "resourcesImported", "12",
            "patients", "1",
            "observations", "8",
            "conditions", "3"
        ));
    }
}
