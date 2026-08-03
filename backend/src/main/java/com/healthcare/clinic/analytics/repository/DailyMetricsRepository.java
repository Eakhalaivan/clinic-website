package com.healthcare.clinic.analytics.repository;

import com.healthcare.clinic.analytics.entity.DailyMetrics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface DailyMetricsRepository extends JpaRepository<DailyMetrics, Long> {
    Optional<DailyMetrics> findByDate(LocalDate date);
}
