package com.healthcare.clinic.ai.repository;
import com.healthcare.clinic.ai.entity.AiAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AiAuditLogRepository extends JpaRepository<AiAuditLog, Long> {}
