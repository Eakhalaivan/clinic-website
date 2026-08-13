package com.healthcare.clinic.emr.repository;

import com.healthcare.clinic.emr.entity.ProcedureRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProcedureRecordRepository extends JpaRepository<ProcedureRecord, Long> {
    List<ProcedureRecord> findByPatientId(Long patientId);
}

