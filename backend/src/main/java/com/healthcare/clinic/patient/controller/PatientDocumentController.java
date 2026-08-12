package com.healthcare.clinic.patient.controller;

import com.healthcare.clinic.identity.entity.User;
import com.healthcare.clinic.patient.entity.PatientDocument;
import com.healthcare.clinic.patient.service.PatientDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/patient/documents")
@RequiredArgsConstructor
public class PatientDocumentController {

    private final PatientDocumentService patientDocumentService;

    @GetMapping
    public ResponseEntity<List<PatientDocument>> getPatientDocuments(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(patientDocumentService.getPatientDocuments(user));
    }

    @PostMapping
    public ResponseEntity<PatientDocument> uploadDocument(
            @AuthenticationPrincipal User user,
            @RequestBody PatientDocument document) {
        return ResponseEntity.ok(patientDocumentService.saveDocumentMetadata(user, document));
    }
}
