package com.healthcare.clinic.emr.repository;

import com.healthcare.clinic.emr.entity.FamilyHistoryEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FamilyHistoryEntryRepository extends JpaRepository<FamilyHistoryEntry, Long> {
    List<FamilyHistoryEntry> findByPatientId(Long patientId);
}

