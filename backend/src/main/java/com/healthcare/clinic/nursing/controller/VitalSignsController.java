package com.healthcare.clinic.nursing.controller;

import com.healthcare.clinic.identity.entity.User;
import com.healthcare.clinic.nursing.entity.VitalSign;
import com.healthcare.clinic.nursing.repository.VitalSignRepository;
import com.healthcare.clinic.patient.entity.PatientProfile;
import com.healthcare.clinic.patient.repository.PatientProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/patients/{patientId}/vitals")
@RequiredArgsConstructor
public class VitalSignsController {

    private final VitalSignRepository vitalSignRepository;
    private final PatientProfileRepository patientProfileRepository;

    @GetMapping
    @PreAuthorize("@nursingSecurity.isAssigned(authentication, #patientId) or hasRole('DOCTOR')")
    public ResponseEntity<List<VitalSign>> getVitalSigns(@PathVariable Long patientId) {
        return ResponseEntity.ok(vitalSignRepository.findByPatientIdOrderByRecordedAtDesc(patientId));
    }

    @PostMapping
    @PreAuthorize("@nursingSecurity.isAssigned(authentication, #patientId)")
    public ResponseEntity<?> addVitalSign(@PathVariable Long patientId, @RequestBody VitalSign vitalSign, @AuthenticationPrincipal User nurse) {
        PatientProfile patient = patientProfileRepository.findById(patientId).orElse(null);
        if (patient == null) return ResponseEntity.notFound().build();

        vitalSign.setPatient(patient);
        vitalSign.setNurse(nurse);
        vitalSign.setRecordedAt(ZonedDateTime.now());
        
        return ResponseEntity.ok(vitalSignRepository.save(vitalSign));
    }
}
