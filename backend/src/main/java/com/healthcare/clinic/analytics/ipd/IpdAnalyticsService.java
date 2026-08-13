package com.healthcare.clinic.analytics.ipd;

import com.healthcare.clinic.analytics.core.AnalyticsBaseDTOs;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class IpdAnalyticsService {

    private final IpdAnalyticsRepository ipdAnalyticsRepository;

    public Map<String, Object> getIpdDashboard(AnalyticsBaseDTOs.AnalyticsFilterRequest filter) {
        ZonedDateTime start = filter.getStartDate() != null 
                ? filter.getStartDate().atStartOfDay(ZoneId.systemDefault()) 
                : ZonedDateTime.now().minusDays(30);
        
        ZonedDateTime end = filter.getEndDate() != null 
                ? filter.getEndDate().plusDays(1).atStartOfDay(ZoneId.systemDefault()).minusSeconds(1) 
                : ZonedDateTime.now();

        List<Object[]> dailyAdmissions = ipdAnalyticsRepository.getDailyAdmissionsVolume(filter.getBranchId(), start, end);
        List<Object[]> wardOccupancy = ipdAnalyticsRepository.getCurrentWardOccupancy();

        long currentlyAdmitted = ipdAnalyticsRepository.countAdmissionsByStatus(filter.getBranchId(), "ADMITTED");
        long recentlyDischarged = ipdAnalyticsRepository.countAdmissionsByStatus(filter.getBranchId(), "DISCHARGED"); // In real life, constrain by date

        return Map.of(
                "admissionsChart", formatAdmissionsChart(dailyAdmissions),
                "wardOccupancyChart", formatWardChart(wardOccupancy),
                "kpis", calculateIpdKpis(currentlyAdmitted, recentlyDischarged, dailyAdmissions)
        );
    }

    private AnalyticsBaseDTOs.ChartDataDto formatAdmissionsChart(List<Object[]> data) {
        List<String> labels = new ArrayList<>();
        List<Object> points = new ArrayList<>();

        for (Object[] row : data) {
            labels.add(row[0].toString());
            points.add(((Number) row[1]).longValue());
        }

        return new AnalyticsBaseDTOs.ChartDataDto(
                "Admissions Over Time", 
                "Date", 
                "Volume", 
                labels, 
                List.of(new AnalyticsBaseDTOs.DatasetDto("Admissions", points, "area"))
        );
    }

    private AnalyticsBaseDTOs.ChartDataDto formatWardChart(List<Object[]> data) {
        List<String> labels = new ArrayList<>();
        List<Object> points = new ArrayList<>();

        for (Object[] row : data) {
            labels.add(row[0].toString());
            points.add(((Number) row[1]).longValue());
        }

        return new AnalyticsBaseDTOs.ChartDataDto(
                "Current Occupancy by Ward", 
                "Ward", 
                "Patients", 
                labels, 
                List.of(new AnalyticsBaseDTOs.DatasetDto("Occupied Beds", points, "bar"))
        );
    }

    private List<AnalyticsBaseDTOs.KPIDto> calculateIpdKpis(long currentlyAdmitted, long recentlyDischarged, List<Object[]> admissionsData) {
        long totalPeriodAdmissions = 0;
        for (Object[] row : admissionsData) {
            totalPeriodAdmissions += ((Number) row[1]).longValue();
        }

        return List.of(
                new AnalyticsBaseDTOs.KPIDto("Currently Admitted", currentlyAdmitted, "", null, null, "NEUTRAL", "/inpatient/dashboard"),
                new AnalyticsBaseDTOs.KPIDto("Period Admissions", totalPeriodAdmissions, "", null, null, "UP", "/inpatient/dashboard"),
                new AnalyticsBaseDTOs.KPIDto("Total Discharges", recentlyDischarged, "", null, null, "UP", "/inpatient/dashboard")
        );
    }
}
