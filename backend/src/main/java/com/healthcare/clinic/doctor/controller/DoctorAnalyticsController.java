package com.healthcare.clinic.doctor.controller;

import com.healthcare.clinic.doctor.dto.DoctorAnalyticsResponse;
import com.healthcare.clinic.doctor.service.DoctorAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.healthcare.clinic.security.SecurityUtils;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/doctor/analytics")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_DOCTOR')")
public class DoctorAnalyticsController {

    private final DoctorAnalyticsService analyticsService;

    @GetMapping
    public ResponseEntity<DoctorAnalyticsResponse> getAnalytics() {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(analyticsService.getAnalyticsForDoctor(currentUserId));
    }
}
