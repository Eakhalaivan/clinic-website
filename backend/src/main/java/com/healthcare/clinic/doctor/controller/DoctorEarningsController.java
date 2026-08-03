package com.healthcare.clinic.doctor.controller;

import com.healthcare.clinic.doctor.dto.DoctorEarningsResponse;
import com.healthcare.clinic.doctor.service.DoctorEarningsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.healthcare.clinic.security.SecurityUtils;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/doctor/earnings")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_DOCTOR')")
public class DoctorEarningsController {

    private final DoctorEarningsService earningsService;

    @GetMapping
    public ResponseEntity<DoctorEarningsResponse> getEarnings() {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(earningsService.getEarningsForDoctor(currentUserId));
    }
}
