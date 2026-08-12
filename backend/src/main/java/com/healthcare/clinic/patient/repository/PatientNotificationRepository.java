package com.healthcare.clinic.patient.repository;

import com.healthcare.clinic.patient.entity.PatientNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientNotificationRepository extends JpaRepository<PatientNotification, Long> {
    List<PatientNotification> findByPatientIdOrderByCreatedAtDesc(Long patientId);
    List<PatientNotification> findByPatientIdAndIsReadFalse(Long patientId);
}
