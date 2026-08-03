package com.healthcare.clinic.superadmin.repository;

import com.healthcare.clinic.superadmin.entity.SuperAdminAuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("superAdminAuditLogRepository")
public interface AuditLogRepository extends JpaRepository<SuperAdminAuditLog, Long> {
    Page<SuperAdminAuditLog> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
