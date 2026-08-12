package com.healthcare.clinic.nursing.repository;

import com.healthcare.clinic.nursing.entity.PainAssessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PainAssessmentRepository extends JpaRepository<PainAssessment, Long> {
    List<PainAssessment> findByPatientIdOrderByAssessedAtDesc(Long patientId);
    List<PainAssessment> findByEncounterIdOrderByAssessedAtDesc(Long encounterId);
}
