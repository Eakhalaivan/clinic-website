package com.healthcare.clinic.engagement.repository;

import com.healthcare.clinic.engagement.entity.PatientWellnessEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientWellnessEnrollmentRepository extends JpaRepository<PatientWellnessEnrollment, Long> {
    List<PatientWellnessEnrollment> findByPatientId(Long patientId);
    List<PatientWellnessEnrollment> findByStatus(PatientWellnessEnrollment.EnrollmentStatus status);
}
