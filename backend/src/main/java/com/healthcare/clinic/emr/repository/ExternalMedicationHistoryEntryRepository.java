package com.healthcare.clinic.emr.repository;

import com.healthcare.clinic.emr.entity.ExternalMedicationHistoryEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ExternalMedicationHistoryEntryRepository extends JpaRepository<ExternalMedicationHistoryEntry, Long> {
    List<ExternalMedicationHistoryEntry> findByPatientId(Long patientId);
}

