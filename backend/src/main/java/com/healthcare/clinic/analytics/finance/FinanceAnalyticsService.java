package com.healthcare.clinic.analytics.finance;

import com.healthcare.clinic.analytics.core.AnalyticsBaseDTOs;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class FinanceAnalyticsService {

    private final FinanceAnalyticsRepository financeAnalyticsRepository;

    public Map<String, Object> getFinanceDashboard(AnalyticsBaseDTOs.AnalyticsFilterRequest filter) {
        ZonedDateTime start = filter.getStartDate() != null 
                ? filter.getStartDate().atStartOfDay(ZoneId.systemDefault()) 
                : ZonedDateTime.now().minusDays(30);
        
        ZonedDateTime end = filter.getEndDate() != null 
                ? filter.getEndDate().plusDays(1).atStartOfDay(ZoneId.systemDefault()).minusSeconds(1) 
                : ZonedDateTime.now();

        LocalDateTime startLdt = start.toLocalDateTime();
        LocalDateTime endLdt = end.toLocalDateTime();

        List<Object[]> dailyRevenue = financeAnalyticsRepository.getDailyRevenue(filter.getBranchId(), startLdt, endLdt);
        List<Object[]> paymentMethods = financeAnalyticsRepository.getRevenueByPaymentMethod(filter.getBranchId(), startLdt, endLdt);
        List<Object[]> statusSummary = financeAnalyticsRepository.getInvoiceStatusSummary(filter.getBranchId(), startLdt, endLdt);

        return Map.of(
                "revenueChart", formatRevenueChart(dailyRevenue),
                "paymentMethodChart", formatPaymentChart(paymentMethods),
                "kpis", calculateFinanceKpis(statusSummary)
        );
    }

    private AnalyticsBaseDTOs.ChartDataDto formatRevenueChart(List<Object[]> data) {
        List<String> labels = new ArrayList<>();
        List<Object> points = new ArrayList<>();

        for (Object[] row : data) {
            labels.add(row[0].toString());
            points.add(row[1] != null ? ((BigDecimal) row[1]).doubleValue() : 0.0);
        }

        return new AnalyticsBaseDTOs.ChartDataDto(
                "Daily Revenue", 
                "Date", 
                "Revenue ($)", 
                labels, 
                List.of(new AnalyticsBaseDTOs.DatasetDto("Revenue", points, "line"))
        );
    }

    private AnalyticsBaseDTOs.ChartDataDto formatPaymentChart(List<Object[]> data) {
        List<String> labels = new ArrayList<>();
        List<Object> points = new ArrayList<>();

        for (Object[] row : data) {
            labels.add(row[0].toString());
            points.add(row[1] != null ? ((BigDecimal) row[1]).doubleValue() : 0.0);
        }

        return new AnalyticsBaseDTOs.ChartDataDto(
                "Revenue by Payment Method", 
                "Method", 
                "Revenue ($)", 
                labels, 
                List.of(new AnalyticsBaseDTOs.DatasetDto("Revenue", points, "pie"))
        );
    }

    private List<AnalyticsBaseDTOs.KPIDto> calculateFinanceKpis(List<Object[]> statusSummary) {
        double totalRevenue = 0;
        double outstanding = 0;
        double cancelled = 0;

        for (Object[] row : statusSummary) {
            String status = row[0].toString();
            double amount = row[1] != null ? ((BigDecimal) row[1]).doubleValue() : 0.0;
            
            if ("PAID".equalsIgnoreCase(status)) {
                totalRevenue += amount;
            } else if ("PENDING".equalsIgnoreCase(status) || "OVERDUE".equalsIgnoreCase(status)) {
                outstanding += amount;
            } else if ("CANCELLED".equalsIgnoreCase(status)) {
                cancelled += amount;
            }
        }

        return List.of(
                new AnalyticsBaseDTOs.KPIDto("Total Collected", totalRevenue, "$", null, null, "UP", "/finance/invoices"),
                new AnalyticsBaseDTOs.KPIDto("Outstanding Balance", outstanding, "$", null, null, "DOWN", "/finance/invoices?status=PENDING"),
                new AnalyticsBaseDTOs.KPIDto("Cancelled Revenue", cancelled, "$", null, null, "NEUTRAL", null)
        );
    }
}
