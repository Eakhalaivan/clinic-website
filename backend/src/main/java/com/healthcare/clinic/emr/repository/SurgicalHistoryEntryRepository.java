package com.healthcare.clinic.emr.repository;

import com.healthcare.clinic.emr.entity.SurgicalHistoryEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SurgicalHistoryEntryRepository extends JpaRepository<SurgicalHistoryEntry, Long> {
    List<SurgicalHistoryEntry> findByPatientId(Long patientId);
}

