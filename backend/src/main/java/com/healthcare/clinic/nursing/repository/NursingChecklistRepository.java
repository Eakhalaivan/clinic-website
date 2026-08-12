package com.healthcare.clinic.nursing.repository;

import com.healthcare.clinic.nursing.entity.NursingChecklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NursingChecklistRepository extends JpaRepository<NursingChecklist, Long> {
    List<NursingChecklist> findByPatientIdOrderByCreatedAtDesc(Long patientId);
    List<NursingChecklist> findByEncounterIdOrderByCreatedAtDesc(Long encounterId);
}
