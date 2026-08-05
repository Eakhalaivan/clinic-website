package com.healthcare.clinic.radiology.controller;

import com.healthcare.clinic.patient.entity.PatientProfile;
import com.healthcare.clinic.patient.repository.PatientProfileRepository;
import com.healthcare.clinic.radiology.entity.RadiologyReport;
import com.healthcare.clinic.radiology.repository.RadiologyReportRepository;
import com.healthcare.clinic.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/patient/radiology-reports")
@RequiredArgsConstructor
public class PatientRadiologyController {

    private final RadiologyReportRepository radiologyReportRepository;
    private final PatientProfileRepository patientProfileRepository;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_PATIENT')")
    public ResponseEntity<List<RadiologyReport>> getMyRadiologyReports() {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        
        Optional<PatientProfile> patientOpt = patientProfileRepository.findByUserId(currentUserId);
        if (patientOpt.isEmpty()) {
            return ResponseEntity.ok(List.of());
        }
        
        Long patientId = patientOpt.get().getId();
        
        // Fetch reports by patient profile ID
        List<RadiologyReport> myReports = radiologyReportRepository.findByRequestPatientId(patientId);
            
        return ResponseEntity.ok(myReports);
    }
}
