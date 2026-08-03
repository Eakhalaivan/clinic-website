package com.healthcare.clinic.analytics.repository;

import com.healthcare.clinic.analytics.entity.DoctorPerformance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorPerformanceRepository extends JpaRepository<DoctorPerformance, Long> {
    Optional<DoctorPerformance> findByDoctorUserIdAndDate(Long doctorUserId, LocalDate date);
    List<DoctorPerformance> findByDate(LocalDate date);
}
