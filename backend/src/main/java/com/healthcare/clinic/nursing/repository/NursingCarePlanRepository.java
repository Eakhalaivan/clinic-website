package com.healthcare.clinic.nursing.repository;

import com.healthcare.clinic.nursing.entity.NursingCarePlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NursingCarePlanRepository extends JpaRepository<NursingCarePlan, Long> {
    List<NursingCarePlan> findByPatientIdOrderByCreatedAtDesc(Long patientId);
    List<NursingCarePlan> findByPatientIdAndStatusOrderByCreatedAtDesc(Long patientId, String status);
    List<NursingCarePlan> findByEncounterId(Long encounterId);
}
