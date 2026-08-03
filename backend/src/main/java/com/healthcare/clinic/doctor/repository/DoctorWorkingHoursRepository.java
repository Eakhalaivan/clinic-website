package com.healthcare.clinic.doctor.repository;

import com.healthcare.clinic.doctor.entity.DoctorWorkingHours;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DoctorWorkingHoursRepository extends JpaRepository<DoctorWorkingHours, Long> {
    List<DoctorWorkingHours> findByDoctorIdAndIsActiveTrue(Long doctorId);
    Optional<DoctorWorkingHours> findByDoctorIdAndDayOfWeek(Long doctorId, Integer dayOfWeek);
}
