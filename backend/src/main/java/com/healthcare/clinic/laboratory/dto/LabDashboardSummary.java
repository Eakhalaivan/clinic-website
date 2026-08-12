package com.healthcare.clinic.laboratory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LabDashboardSummary {
    private long totalRequests;
    private Map<String, Long> statusCounts;
    private Map<String, Long> priorityCounts;
    private long requestsToday;
    private Double averageTurnaroundTimeHours;
    private java.math.BigDecimal totalRevenue;
    private Map<String, Long> technicianStats;
}
