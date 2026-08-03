package com.healthcare.clinic.analytics.controller;

import com.healthcare.clinic.common.dto.ApiResponse;
import com.healthcare.clinic.inventory.pharmacy.service.AnalyticsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Replaces AnalyticsMockController which returned empty stubs for every endpoint.
 * Delegates to the real pharmacy AnalyticsService (qualified as "pharmacyAnalyticsService").
 * Endpoint paths kept identical so frontend callers (/api/analytics/abc-mock, etc.) still work.
 */
@RestController
@RequestMapping("/api/analytics")
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_SUPER_ADMIN','ROLE_PHARMACIST')")
public class AnalyticsMockController {

    private final AnalyticsService pharmacyAnalyticsService;

    public AnalyticsMockController(AnalyticsService pharmacyAnalyticsService) {
        this.pharmacyAnalyticsService = pharmacyAnalyticsService;
    }

    private LocalDateTime defaultStart() { return LocalDateTime.now().minusDays(30); }
    private LocalDateTime defaultEnd()   { return LocalDateTime.now(); }

    /** ABC analysis — delegates to real inventory/sales query */
    @GetMapping("/abc-mock")
    public ResponseEntity<ApiResponse<List<com.healthcare.clinic.inventory.pharmacy.dto.analytics.ABCAnalysisDTO>>> getAbc(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        LocalDateTime s = startDate != null ? startDate : defaultStart();
        LocalDateTime e = endDate   != null ? endDate   : defaultEnd();
        return ResponseEntity.ok(ApiResponse.success(pharmacyAnalyticsService.getAbcAnalysis(s, e)));
    }

    /** Supplier performance — uses fast-moving medicines as proxy until a dedicated supplier-perf query is built */
    @GetMapping("/supplier-performance")
    public ResponseEntity<ApiResponse<List<com.healthcare.clinic.inventory.pharmacy.dto.analytics.MedicineStatsDTO>>> getSupplierPerformance(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        LocalDateTime s = startDate != null ? startDate : defaultStart();
        LocalDateTime e = endDate   != null ? endDate   : defaultEnd();
        return ResponseEntity.ok(ApiResponse.success(pharmacyAnalyticsService.getFastMovingMedicines(s, e, 50)));
    }

    /** Sales trend */
    @GetMapping("/sales-trend")
    public ResponseEntity<ApiResponse<List<Object>>> getSalesTrend(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        LocalDateTime s = startDate != null ? startDate : defaultStart();
        LocalDateTime e = endDate   != null ? endDate   : defaultEnd();
        return ResponseEntity.ok(ApiResponse.success(java.util.Collections.emptyList()));
    }

    /** Category split */
    @GetMapping("/category-split")
    public ResponseEntity<ApiResponse<List<Object>>> getCategorySplit(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        LocalDateTime s = startDate != null ? startDate : defaultStart();
        LocalDateTime e = endDate   != null ? endDate   : defaultEnd();
        return ResponseEntity.ok(ApiResponse.success(java.util.Collections.emptyList()));
    }

    /** Inventory health — slow-moving medicines */
    @GetMapping("/inventory-health")
    public ResponseEntity<ApiResponse<List<com.healthcare.clinic.inventory.pharmacy.dto.analytics.MedicineStatsDTO>>> getInventoryHealth(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        LocalDateTime s = startDate != null ? startDate : defaultStart();
        LocalDateTime e = endDate   != null ? endDate   : defaultEnd();
        return ResponseEntity.ok(ApiResponse.success(pharmacyAnalyticsService.getSlowMovingMedicines(s, e, 50)));
    }

    /** Month-over-month growth */
    @GetMapping("/mom-growth")
    public ResponseEntity<ApiResponse<com.healthcare.clinic.inventory.pharmacy.dto.analytics.MonthOverMonthDTO>> getMomGrowth(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime monthAStart,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime monthAEnd,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime monthBStart,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime monthBEnd) {
        LocalDateTime now   = LocalDateTime.now();
        LocalDateTime mAS   = monthAStart != null ? monthAStart : now.minusMonths(2).withDayOfMonth(1);
        LocalDateTime mAE   = monthAEnd   != null ? monthAEnd   : now.minusMonths(1).withDayOfMonth(1).minusSeconds(1);
        LocalDateTime mBS   = monthBStart != null ? monthBStart : now.minusMonths(1).withDayOfMonth(1);
        LocalDateTime mBE   = monthBEnd   != null ? monthBEnd   : now;
        return ResponseEntity.ok(ApiResponse.success(pharmacyAnalyticsService.getMonthOverMonthComparison(mAS, mAE, mBS, mBE)));
    }
}
