package com.healthcare.clinic.analytics.lab;

import com.healthcare.clinic.analytics.core.AnalyticsBaseDTOs;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class LabAnalyticsService {

    private final LabAnalyticsRepository labAnalyticsRepository;

    public Map<String, Object> getLabDashboard(AnalyticsBaseDTOs.AnalyticsFilterRequest filter) {
        ZonedDateTime start = filter.getStartDate() != null 
                ? filter.getStartDate().atStartOfDay(ZoneId.systemDefault()) 
                : ZonedDateTime.now().minusDays(30);
        
        ZonedDateTime end = filter.getEndDate() != null 
                ? filter.getEndDate().plusDays(1).atStartOfDay(ZoneId.systemDefault()).minusSeconds(1) 
                : ZonedDateTime.now();

        List<Object[]> dailyVolume = labAnalyticsRepository.getDailyTestVolume(filter.getBranchId(), start, end);
        List<Object[]> testTypeVolume = labAnalyticsRepository.getVolumeByTestType(filter.getBranchId(), start, end);
        List<Object[]> abnormalStats = labAnalyticsRepository.getAbnormalityStats(filter.getBranchId(), start, end);

        return Map.of(
                "volumeChart", formatVolumeChart(dailyVolume),
                "testTypeChart", formatTestTypeChart(testTypeVolume),
                "kpis", calculateLabKpis(dailyVolume, abnormalStats)
        );
    }

    private AnalyticsBaseDTOs.ChartDataDto formatVolumeChart(List<Object[]> data) {
        List<String> labels = new ArrayList<>();
        List<Object> points = new ArrayList<>();

        for (Object[] row : data) {
            labels.add(row[0].toString());
            points.add(((Number) row[1]).longValue());
        }

        return new AnalyticsBaseDTOs.ChartDataDto(
                "Daily Test Requests", 
                "Date", 
                "Volume", 
                labels, 
                List.of(new AnalyticsBaseDTOs.DatasetDto("Tests Requested", points, "bar"))
        );
    }

    private AnalyticsBaseDTOs.ChartDataDto formatTestTypeChart(List<Object[]> data) {
        List<String> labels = new ArrayList<>();
        List<Object> points = new ArrayList<>();

        // Take top 5 and group rest as 'Other'
        long otherCount = 0;
        for (int i = 0; i < data.size(); i++) {
            if (i < 5) {
                labels.add(data.get(i)[0].toString());
                points.add(((Number) data.get(i)[1]).longValue());
            } else {
                otherCount += ((Number) data.get(i)[1]).longValue();
            }
        }
        if (otherCount > 0) {
            labels.add("Other");
            points.add(otherCount);
        }

        return new AnalyticsBaseDTOs.ChartDataDto(
                "Volume by Test Type", 
                "Test", 
                "Volume", 
                labels, 
                List.of(new AnalyticsBaseDTOs.DatasetDto("Count", points, "pie"))
        );
    }

    private List<AnalyticsBaseDTOs.KPIDto> calculateLabKpis(List<Object[]> volumeData, List<Object[]> abnormalStats) {
        long totalTests = 0;
        for (Object[] row : volumeData) {
            totalTests += ((Number) row[1]).longValue();
        }

        long totalResults = 0;
        long abnormalResults = 0;
        if (abnormalStats != null && !abnormalStats.isEmpty() && abnormalStats.get(0)[0] != null) {
            totalResults = ((Number) abnormalStats.get(0)[0]).longValue();
            abnormalResults = abnormalStats.get(0)[1] != null ? ((Number) abnormalStats.get(0)[1]).longValue() : 0;
        }

        double abnormalRate = totalResults > 0 ? ((double) abnormalResults / totalResults) * 100 : 0.0;

        return List.of(
                new AnalyticsBaseDTOs.KPIDto("Total Requests", totalTests, "", null, null, "UP", "/lab/dashboard"),
                new AnalyticsBaseDTOs.KPIDto("Abnormal Results", abnormalResults, "", null, null, "UP", "/lab/results?abnormal=true"),
                new AnalyticsBaseDTOs.KPIDto("Abnormality Rate", Math.round(abnormalRate * 10) / 10.0, "%", null, null, "NEUTRAL", null)
        );
    }
}
