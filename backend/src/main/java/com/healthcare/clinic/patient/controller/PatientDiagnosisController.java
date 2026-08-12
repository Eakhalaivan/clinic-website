package com.healthcare.clinic.patient.controller;

import com.healthcare.clinic.identity.entity.User;
import com.healthcare.clinic.patient.entity.PatientDiagnosis;
import com.healthcare.clinic.patient.service.PatientDiagnosisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/doctor/patients/{patientId}/diagnoses")
@RequiredArgsConstructor
public class PatientDiagnosisController {

    private final PatientDiagnosisService diagnosisService;

    @GetMapping
    public ResponseEntity<List<PatientDiagnosis>> getDiagnoses(@PathVariable Long patientId) {
        return ResponseEntity.ok(diagnosisService.getDiagnosesForPatient(patientId));
    }

    @PostMapping
    public ResponseEntity<PatientDiagnosis> addDiagnosis(
            @AuthenticationPrincipal User user,
            @PathVariable Long patientId,
            @RequestBody PatientDiagnosis diagnosis) {
        diagnosis.setPatientId(patientId);
        return ResponseEntity.ok(diagnosisService.addDiagnosis(user, diagnosis));
    }

    @PostMapping("/{diagnosisId}/resolve")
    public ResponseEntity<PatientDiagnosis> resolveDiagnosis(@PathVariable Long diagnosisId) {
        return ResponseEntity.ok(diagnosisService.resolveDiagnosis(diagnosisId));
    }
}
