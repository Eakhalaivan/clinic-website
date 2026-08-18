package com.healthcare.clinic.admin.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminDashboardMetricsDto {
    private long totalPatients;
    private long totalDoctors;
    private long totalStaff;
    private long todaysAppointments;
    private long pendingAppointments;
    private long completedConsultations;
    private long activeUsers;
    private long inactiveUsers;
    private long pendingLabRequests;
    private long pendingPharmacyPrescriptions;
    private long lowStockMedicines;
    private long expiringMedicines;
    private java.math.BigDecimal todaysRevenue;
    private java.math.BigDecimal outstandingPayments;
}
