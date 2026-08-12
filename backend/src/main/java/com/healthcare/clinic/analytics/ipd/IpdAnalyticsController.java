package com.healthcare.clinic.analytics.ipd;

import com.healthcare.clinic.analytics.core.AnalyticsBaseDTOs;
import com.healthcare.clinic.analytics.core.AnalyticsContextFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/analytics/ipd")
@RequiredArgsConstructor
public class IpdAnalyticsController {

    private final IpdAnalyticsService ipdAnalyticsService;
    private final AnalyticsContextFilter analyticsContextFilter;

    @GetMapping("/dashboard")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'SYSTEM_ADMIN', 'BRANCH_ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<Map<String, Object>> getIpdDashboard(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Long branchId,
            @RequestParam(required = false) String timeRange) {

        Long safeBranchId = analyticsContextFilter.getSafeBranchId(branchId);

        AnalyticsBaseDTOs.AnalyticsFilterRequest filter = AnalyticsBaseDTOs.AnalyticsFilterRequest.builder()
                .startDate(startDate)
                .endDate(endDate)
                .branchId(safeBranchId)
                .timeRange(timeRange)
                .build();

        return ResponseEntity.ok(ipdAnalyticsService.getIpdDashboard(filter));
    }
}
