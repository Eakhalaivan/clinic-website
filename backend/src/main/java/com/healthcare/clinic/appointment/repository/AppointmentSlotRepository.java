package com.healthcare.clinic.appointment.repository;

import com.healthcare.clinic.appointment.entity.AppointmentSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface AppointmentSlotRepository extends JpaRepository<AppointmentSlot, Long> {
    List<AppointmentSlot> findByDoctorUserIdAndStartTimeBetweenAndIsBookedFalse(Long userId, ZonedDateTime start, ZonedDateTime end);
    List<AppointmentSlot> findByDoctorUserIdAndStartTimeBetween(Long userId, ZonedDateTime start, ZonedDateTime end);
    void deleteByDoctorUserIdAndStartTimeBetweenAndIsBookedFalse(Long userId, ZonedDateTime start, ZonedDateTime end);
}
