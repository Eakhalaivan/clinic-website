package com.healthcare.clinic.patient.repository;

import com.healthcare.clinic.patient.entity.PatientDiagnosis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientDiagnosisRepository extends JpaRepository<PatientDiagnosis, Long> {
    List<PatientDiagnosis> findByPatientIdOrderByCreatedAtDesc(Long patientId);
    List<PatientDiagnosis> findByEncounterId(Long encounterId);
}
