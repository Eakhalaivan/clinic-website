package com.healthcare.clinic.analytics.service;

import com.healthcare.clinic.analytics.entity.DailyMetrics;
import com.healthcare.clinic.analytics.entity.DoctorPerformance;
import com.healthcare.clinic.analytics.repository.DailyMetricsRepository;
import com.healthcare.clinic.analytics.repository.DoctorPerformanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final DailyMetricsRepository dailyMetricsRepository;
    private final DoctorPerformanceRepository doctorPerformanceRepository;

    @Transactional(readOnly = true)
    public List<DailyMetrics> getAllDailyMetrics() {
        return dailyMetricsRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<DoctorPerformance> getDoctorPerformanceByDate(LocalDate date) {
        return doctorPerformanceRepository.findByDate(date);
    }

    @Transactional(readOnly = true)
    public List<DoctorPerformance> getAllDoctorPerformances() {
        return doctorPerformanceRepository.findAll();
    }
}
