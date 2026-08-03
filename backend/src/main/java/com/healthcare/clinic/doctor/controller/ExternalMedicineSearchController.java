package com.healthcare.clinic.doctor.controller;

import com.healthcare.clinic.doctor.dto.ExternalMedicineDto;
import com.healthcare.clinic.doctor.service.ExternalMedicineSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/doctor/medicines")
@RequiredArgsConstructor
public class ExternalMedicineSearchController {

    private final ExternalMedicineSearchService externalMedicineSearchService;

    @GetMapping("/external-search")
    @PreAuthorize("hasAuthority('ROLE_DOCTOR')")
    public ResponseEntity<List<ExternalMedicineDto>> externalSearch(@RequestParam String name) {
        return ResponseEntity.ok(externalMedicineSearchService.searchMedicines(name));
    }
}
