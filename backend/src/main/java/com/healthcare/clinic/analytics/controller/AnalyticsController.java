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
import lombok.Data;
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

    // --- Phase 4 Predictive Analytics Mock Endpoints ---

    @GetMapping("/predictive/patient-risk")
    @PreAuthorize("hasAuthority('ROLE_DOCTOR') or hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPER_ADMIN')")
    public ResponseEntity<List<RiskPrediction>> getPatientRiskPredictions() {
        return ResponseEntity.ok(List.of(
            new RiskPrediction(101L, "John Doe", "Cardiovascular Event", 0.85, "High cholesterol, Hypertension history", "Recommend stress test and adjust statin dose"),
            new RiskPrediction(102L, "Jane Smith", "Type 2 Diabetes Onset", 0.72, "Rising HbA1c, Family history", "Dietary intervention, schedule follow-up in 3 months"),
            new RiskPrediction(103L, "Robert Brown", "COPD Exacerbation", 0.45, "Recent seasonal allergies", "Review inhaler technique")
        ));
    }

    @GetMapping("/predictive/no-show")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPER_ADMIN') or hasAuthority('ROLE_RECEPTION')")
    public ResponseEntity<List<NoShowPrediction>> getNoShowPredictions() {
        return ResponseEntity.ok(List.of(
            new NoShowPrediction(201L, "Alice Johnson", "Tomorrow 10:00 AM", 0.90, "Multiple previous no-shows, unconfirmed SMS"),
            new NoShowPrediction(202L, "Michael Lee", "Tomorrow 02:30 PM", 0.65, "Lives > 20 miles away, traffic predicted")
        ));
    }

    @GetMapping("/predictive/resource-demand")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPER_ADMIN')")
    public ResponseEntity<ResourceDemandPrediction> getResourceDemand() {
        return ResponseEntity.ok(
            new ResourceDemandPrediction(
                "Next 7 Days",
                List.of(
                    new DemandItem("ICU Beds", "High", "Predicted 85% occupancy due to flu season spike"),
                    new DemandItem("Ventilators", "Medium", "Adequate supply, but nearing buffer limit"),
                    new DemandItem("Nursing Staff (Night Shift)", "Critical", "Predicted shortfall of 3 nurses on Friday")
                )
            )
        );
    }

    @Data
    public static class RiskPrediction {
        private final Long patientId;
        private final String patientName;
        private final String condition;
        private final double riskScore;
        private final String factors;
        private final String recommendation;
    }

    @Data
    public static class NoShowPrediction {
        private final Long appointmentId;
        private final String patientName;
        private final String time;
        private final double probability;
        private final String reasoning;
    }

    @Data
    public static class ResourceDemandPrediction {
        private final String timeframe;
        private final List<DemandItem> items;
    }

    @Data
    public static class DemandItem {
        private final String resource;
        private final String predictedDemand;
        private final String insights;
    }
}
