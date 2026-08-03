package com.healthcare.clinic.doctor.repository;

import com.healthcare.clinic.doctor.entity.DoctorScheduleOverride;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DoctorScheduleOverrideRepository extends JpaRepository<DoctorScheduleOverride, Long> {
    List<DoctorScheduleOverride> findByDoctorIdAndOverrideDateBetween(Long doctorId, LocalDate start, LocalDate end);
    Optional<DoctorScheduleOverride> findByDoctorIdAndOverrideDate(Long doctorId, LocalDate date);
    void deleteByDoctorIdAndOverrideDate(Long doctorId, LocalDate date);
}
