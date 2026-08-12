package com.healthcare.clinic.nursing.controller;

import com.healthcare.clinic.nursing.dto.MonitoredPatientDTO;
import com.healthcare.clinic.nursing.service.PatientMonitoringService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/nursing/monitoring")
@RequiredArgsConstructor
public class PatientMonitoringController {

    private final PatientMonitoringService patientMonitoringService;

    @GetMapping("/wards/{wardId}/patients")
    @PreAuthorize("hasAnyRole('NURSE', 'CHARGE_NURSE', 'DOCTOR', 'SUPER_ADMIN')")
    public ResponseEntity<List<MonitoredPatientDTO>> getMonitoredPatients(@PathVariable Long wardId) {
        return ResponseEntity.ok(patientMonitoringService.getMonitoredPatientsByWard(wardId));
    }
}
