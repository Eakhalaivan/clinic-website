package com.healthcare.clinic.nursing.controller;

import com.healthcare.clinic.nursing.dto.FallRiskAssessmentRequest;
import com.healthcare.clinic.nursing.dto.NursingCarePlanRequest;
import com.healthcare.clinic.nursing.dto.NursingNoteRequest;
import com.healthcare.clinic.nursing.dto.PainAssessmentRequest;
import com.healthcare.clinic.nursing.entity.FallRiskAssessment;
import com.healthcare.clinic.nursing.entity.NursingCarePlan;
import com.healthcare.clinic.nursing.entity.NursingNote;
import com.healthcare.clinic.nursing.entity.PainAssessment;
import com.healthcare.clinic.nursing.service.NursingDocumentationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nursing/documentation")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('NURSE', 'CHARGE_NURSE')")
public class NursingDocumentationController {

    private final NursingDocumentationService documentationService;

    @PostMapping("/notes")
    public ResponseEntity<NursingNote> createNote(@RequestBody NursingNoteRequest request) {
        return ResponseEntity.ok(documentationService.createNursingNote(request));
    }

    @GetMapping("/notes/{patientId}")
    public ResponseEntity<List<NursingNote>> getNotes(@PathVariable Long patientId) {
        return ResponseEntity.ok(documentationService.getPatientNursingNotes(patientId));
    }

    @PostMapping("/care-plans")
    public ResponseEntity<NursingCarePlan> createCarePlan(@RequestBody NursingCarePlanRequest request) {
        return ResponseEntity.ok(documentationService.createCarePlan(request));
    }

    @GetMapping("/care-plans/{patientId}")
    public ResponseEntity<List<NursingCarePlan>> getCarePlans(@PathVariable Long patientId) {
        return ResponseEntity.ok(documentationService.getPatientCarePlans(patientId));
    }

    @PostMapping("/fall-risk")
    public ResponseEntity<FallRiskAssessment> createFallRiskAssessment(@RequestBody FallRiskAssessmentRequest request) {
        return ResponseEntity.ok(documentationService.createFallRiskAssessment(request));
    }

    @GetMapping("/fall-risk/{patientId}")
    public ResponseEntity<List<FallRiskAssessment>> getFallRiskAssessments(@PathVariable Long patientId) {
        return ResponseEntity.ok(documentationService.getPatientFallRiskAssessments(patientId));
    }

    @PostMapping("/pain")
    public ResponseEntity<PainAssessment> createPainAssessment(@RequestBody PainAssessmentRequest request) {
        return ResponseEntity.ok(documentationService.createPainAssessment(request));
    }

    @GetMapping("/pain/{patientId}")
    public ResponseEntity<List<PainAssessment>> getPainAssessments(@PathVariable Long patientId) {
        return ResponseEntity.ok(documentationService.getPatientPainAssessments(patientId));
    }
}
