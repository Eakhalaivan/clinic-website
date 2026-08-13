package com.healthcare.clinic.analytics.clinical;

import com.healthcare.clinic.analytics.core.AnalyticsBaseDTOs;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ClinicalAnalyticsService {

    private final ClinicalAnalyticsRepository clinicalAnalyticsRepository;

    public Map<String, Object> getClinicalDashboard(AnalyticsBaseDTOs.AnalyticsFilterRequest filter) {
        ZonedDateTime start = filter.getStartDate() != null 
                ? filter.getStartDate().atStartOfDay(ZoneId.systemDefault()) 
                : ZonedDateTime.now().minusDays(30);
        
        ZonedDateTime end = filter.getEndDate() != null 
                ? filter.getEndDate().plusDays(1).atStartOfDay(ZoneId.systemDefault()).minusSeconds(1) 
                : ZonedDateTime.now();

        List<Object[]> followUpRates = clinicalAnalyticsRepository.getFollowUpCompletionRates(filter.getBranchId(), start, end);
        List<Object[]> prescriptionVolume = clinicalAnalyticsRepository.getDailyPrescriptionVolume(filter.getBranchId(), start, end);

        return Map.of(
                "prescriptionChart", formatPrescriptionChart(prescriptionVolume),
                "kpis", calculateClinicalKpis(followUpRates, prescriptionVolume),
                "followUpChart", formatFollowUpChart(followUpRates)
        );
    }

    private AnalyticsBaseDTOs.ChartDataDto formatPrescriptionChart(List<Object[]> data) {
        List<String> labels = new ArrayList<>();
        List<Object> points = new ArrayList<>();

        for (Object[] row : data) {
            labels.add(row[0].toString());
            points.add(((Number) row[1]).longValue());
        }

        return new AnalyticsBaseDTOs.ChartDataDto(
                "Daily Prescription Volume", 
                "Date", 
                "Volume", 
                labels, 
                List.of(new AnalyticsBaseDTOs.DatasetDto("Prescriptions", points, "line"))
        );
    }

    private AnalyticsBaseDTOs.ChartDataDto formatFollowUpChart(List<Object[]> data) {
        List<String> labels = new ArrayList<>();
        List<Object> points = new ArrayList<>();

        for (Object[] row : data) {
            labels.add(row[0].toString());
            points.add(((Number) row[1]).longValue());
        }

        return new AnalyticsBaseDTOs.ChartDataDto(
                "Follow-up Outcomes", 
                "Status", 
                "Volume", 
                labels, 
                List.of(new AnalyticsBaseDTOs.DatasetDto("Count", points, "pie"))
        );
    }

    private List<AnalyticsBaseDTOs.KPIDto> calculateClinicalKpis(List<Object[]> followUpData, List<Object[]> pxData) {
        long totalPrescriptions = 0;
        for (Object[] row : pxData) {
            totalPrescriptions += ((Number) row[1]).longValue();
        }

        long completedFollowups = 0;
        long missedFollowups = 0;

        for (Object[] row : followUpData) {
            String status = row[0].toString();
            long count = ((Number) row[1]).longValue();
            
            if ("COMPLETED".equalsIgnoreCase(status)) {
                completedFollowups += count;
            } else if ("MISSED".equalsIgnoreCase(status)) {
                missedFollowups += count;
            }
        }

        return List.of(
                new AnalyticsBaseDTOs.KPIDto("Total Prescriptions", totalPrescriptions, "", null, null, "UP", "/clinical/prescriptions"),
                new AnalyticsBaseDTOs.KPIDto("Follow-ups Completed", completedFollowups, "", null, null, "UP", "/clinical/follow-ups"),
                new AnalyticsBaseDTOs.KPIDto("Follow-ups Missed", missedFollowups, "", null, null, "DOWN", "/clinical/follow-ups?status=MISSED")
        );
    }
}
