package com.healthcare.clinic.nursing.repository;

import com.healthcare.clinic.nursing.entity.MedicationAdministrationRecord;
import com.healthcare.clinic.nursing.entity.MarStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MedicationAdministrationRecordRepository extends JpaRepository<MedicationAdministrationRecord, Long> {
    List<MedicationAdministrationRecord> findByPatientId(Long patientId);
    List<MedicationAdministrationRecord> findByPatientIdAndStatus(Long patientId, MarStatus status);
}
