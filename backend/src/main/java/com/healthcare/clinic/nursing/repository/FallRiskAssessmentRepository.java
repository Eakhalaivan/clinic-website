package com.healthcare.clinic.nursing.repository;

import com.healthcare.clinic.nursing.entity.FallRiskAssessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FallRiskAssessmentRepository extends JpaRepository<FallRiskAssessment, Long> {
    List<FallRiskAssessment> findByPatientIdOrderByAssessedAtDesc(Long patientId);
    List<FallRiskAssessment> findByEncounterIdOrderByAssessedAtDesc(Long encounterId);
}
