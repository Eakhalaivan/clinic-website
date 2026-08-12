package com.healthcare.clinic.patient.repository;

import com.healthcare.clinic.patient.entity.PatientNotificationPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientNotificationPreferenceRepository extends JpaRepository<PatientNotificationPreference, Long> {
    List<PatientNotificationPreference> findByPatientId(Long patientId);
    Optional<PatientNotificationPreference> findByPatientIdAndCategory(Long patientId, String category);
}
