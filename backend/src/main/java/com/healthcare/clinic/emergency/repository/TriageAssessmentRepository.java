package com.healthcare.clinic.emergency.repository;

import com.healthcare.clinic.emergency.entity.TriageAssessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TriageAssessmentRepository extends JpaRepository<TriageAssessment, Long> {
    Optional<TriageAssessment> findByEmergencyEncounterId(Long emergencyEncounterId);
}
