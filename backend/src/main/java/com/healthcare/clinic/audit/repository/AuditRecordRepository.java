package com.healthcare.clinic.audit.repository;

import com.healthcare.clinic.audit.entity.AuditRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuditRecordRepository extends JpaRepository<AuditRecord, Long> {

    Optional<AuditRecord> findTopByOrderByIdDesc();

    Page<AuditRecord> findByPatientIdOrderByCreatedAtDesc(Long patientId, Pageable pageable);

    @Query("SELECT a FROM AuditRecord a WHERE " +
           "(:patientId IS NULL OR a.patientId = :patientId) AND " +
           "(:actorId IS NULL OR a.actorId = :actorId) AND " +
           "(:moduleName IS NULL OR a.moduleName = :moduleName) AND " +
           "(:actionName IS NULL OR a.actionName = :actionName) AND " +
           "(:outcome IS NULL OR a.outcome = :outcome) AND " +
           "(:tenantId IS NULL OR a.tenantId = :tenantId)")
    Page<AuditRecord> searchAuditLogs(Long patientId, Long actorId, String moduleName, String actionName, String outcome, Long tenantId, Pageable pageable);
}
