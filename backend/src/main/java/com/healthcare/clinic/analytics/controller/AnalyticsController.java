package com.healthcare.clinic.analytics.controller;

import com.healthcare.clinic.analytics.entity.DailyMetrics;
import com.healthcare.clinic.analytics.entity.DoctorPerformance;
import com.healthcare.clinic.analytics.service.AnalyticsService;
import com.healthcare.clinic.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/daily-metrics")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<DailyMetrics>> getDailyMetrics() {
        return ResponseEntity.ok(analyticsService.getAllDailyMetrics());
    }

    @GetMapping("/doctor-performance")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_BRANCH_ADMIN')")
    public ResponseEntity<List<DoctorPerformance>> getDoctorPerformance(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(analyticsService.getDoctorPerformanceByDate(date));
    }

    @GetMapping("/doctor-performance/{doctorId}")
    @PreAuthorize("hasAuthority('ROLE_DOCTOR') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<DoctorPerformance>> getDoctorPerformanceForDoctor(
            @PathVariable Long doctorId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        SecurityUtils.assertOwnerOrAdmin(doctorId);
        
        List<DoctorPerformance> performanceList = analyticsService.getAllDoctorPerformances();
        
        List<DoctorPerformance> filtered = performanceList.stream()
            .filter(dp -> dp.getDoctorUserId().equals(doctorId))
            .filter(dp -> date == null || dp.getDate().equals(date))
            .collect(Collectors.toList());
            
        return ResponseEntity.ok(filtered);
    }
}
