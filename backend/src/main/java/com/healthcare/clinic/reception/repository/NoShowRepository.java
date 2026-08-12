package com.healthcare.clinic.reception.repository;

import com.healthcare.clinic.reception.entity.NoShow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoShowRepository extends JpaRepository<NoShow, Long> {
    List<NoShow> findByPatientIdOrderByRecordedAtDesc(Long patientId);
    List<NoShow> findByAppointmentId(Long appointmentId);
}
