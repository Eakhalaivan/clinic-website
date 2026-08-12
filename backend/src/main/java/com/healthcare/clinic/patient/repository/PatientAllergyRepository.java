package com.healthcare.clinic.patient.repository;

import com.healthcare.clinic.patient.entity.PatientAllergy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientAllergyRepository extends JpaRepository<PatientAllergy, Long> {
    List<PatientAllergy> findByPatientIdAndStatusNot(Long patientId, String status);
    List<PatientAllergy> findByPatientIdOrderByCreatedAtDesc(Long patientId);
}
