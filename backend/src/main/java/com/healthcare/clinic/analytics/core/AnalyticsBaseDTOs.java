package com.healthcare.clinic.analytics.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

public class AnalyticsBaseDTOs {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AnalyticsFilterRequest {
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        private LocalDate startDate;

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        private LocalDate endDate;

        private Long branchId;
        private Long departmentId;
        private Long doctorId;
        private String timeRange; // e.g. "TODAY", "LAST_7_DAYS", "THIS_MONTH"
        private String period; // e.g. "DAILY", "WEEKLY", "MONTHLY"
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class KPIDto {
        private String name;
        private Object value;
        private String unit;
        private Double previousValue;
        private Double changePercentage;
        private String trendDirection; // UP, DOWN, NEUTRAL
        private String drillDownContext; // E.g., /api/opd/appointments?status=no_show
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ChartDataDto {
        private String chartTitle;
        private String xAxisLabel;
        private String yAxisLabel;
        private List<String> labels;
        private List<DatasetDto> datasets;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DatasetDto {
        private String label;
        private List<Object> data;
        private String type; // bar, line, etc.
    }
}
