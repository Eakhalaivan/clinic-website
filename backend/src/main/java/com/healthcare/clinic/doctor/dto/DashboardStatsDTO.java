package com.healthcare.clinic.doctor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDTO {

    private int todayAppointments;
    private int totalPatients;
    private int prescriptionsToday;
    private int followUpsToday;
    private BigDecimal todayEarnings;
    
    private int waitingCount;
    private int emergencyCount;
    private int completedCount;

    private List<Activity> recentActivity;
    private List<ChartData> weeklyPatientsChart;
    private List<ChartData> monthlyRevenueChart;
    private List<ChartData> appointmentTrendsChart;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Activity {
        private String id;
        private String type; // e.g., "CONSULTATION", "PRESCRIPTION", "PATIENT_ADDED"
        private String description;
        private String date;
        private String time;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChartData {
        private String name;
        private Number value;
    }
}
