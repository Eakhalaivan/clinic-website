package com.healthcare.clinic.nursing.repository;

import com.healthcare.clinic.nursing.entity.NursingNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NursingNoteRepository extends JpaRepository<NursingNote, Long> {
    List<NursingNote> findByPatientIdOrderByRecordedAtDesc(Long patientId);
    List<NursingNote> findByNurseIdOrderByRecordedAtDesc(Long nurseId);
}
