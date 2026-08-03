package com.healthcare.clinic.doctor.controller;

import com.healthcare.clinic.doctor.dto.DashboardStatsDTO;
import com.healthcare.clinic.doctor.service.DoctorDashboardService;
import com.healthcare.clinic.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/doctor")
@RequiredArgsConstructor
public class DoctorDashboardController {

    private final DoctorDashboardService dashboardService;

    @PreAuthorize("hasAuthority('ROLE_DOCTOR')")
    @GetMapping("/dashboard-stats")
    public ResponseEntity<DashboardStatsDTO> getDashboardStats() {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(dashboardService.getDashboardStats(currentUserId));
    }
}
