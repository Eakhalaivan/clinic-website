package com.healthcare.clinic.nursing.controller;

import com.healthcare.clinic.nursing.entity.MedicationAdministrationRecord;
import com.healthcare.clinic.nursing.service.MedicationAdministrationService;
import com.healthcare.clinic.security.SecurityUtils;
import com.healthcare.clinic.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/nursing/mar")
@RequiredArgsConstructor
public class MedicationAdministrationController {
    
    private final MedicationAdministrationService marService;
    
    @GetMapping
    @PreAuthorize("hasAnyRole('NURSE', 'ADMIN', 'DOCTOR')")
    public ResponseEntity<ApiResponse<List<MedicationAdministrationRecord>>> getAllRecords() {
        return ResponseEntity.ok(ApiResponse.success(marService.getAllRecords()));
    }
    
    @PostMapping
    @PreAuthorize("hasAnyRole('NURSE', 'ADMIN', 'DOCTOR')")
    public ResponseEntity<ApiResponse<MedicationAdministrationRecord>> createRecord(@RequestBody MedicationAdministrationRecord record) {
        return ResponseEntity.ok(ApiResponse.success(marService.createRecord(record), "MAR created"));
    }
    
    @PostMapping("/{id}/administer")
    @PreAuthorize("hasAnyRole('NURSE', 'ADMIN', 'DOCTOR')")
    public ResponseEntity<ApiResponse<MedicationAdministrationRecord>> administerMedication(@PathVariable Long id) {
        Long nurseId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(ApiResponse.success(marService.markAsGiven(id, nurseId), "Medication marked as given"));
    }
}
