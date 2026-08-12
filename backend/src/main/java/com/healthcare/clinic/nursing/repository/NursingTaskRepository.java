package com.healthcare.clinic.nursing.repository;

import com.healthcare.clinic.nursing.entity.NursingTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NursingTaskRepository extends JpaRepository<NursingTask, Long> {
    List<NursingTask> findByPatientIdOrderByDueTimeAsc(Long patientId);
    List<NursingTask> findByAssignedToIdAndStatusOrderByDueTimeAsc(Long nurseId, String status);
    List<NursingTask> findByEncounterIdOrderByDueTimeAsc(Long encounterId);
}
