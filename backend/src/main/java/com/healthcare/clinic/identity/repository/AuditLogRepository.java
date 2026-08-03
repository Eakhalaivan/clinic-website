package com.healthcare.clinic.identity.repository;

import com.healthcare.clinic.identity.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("identityAuditLogRepository")
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}
