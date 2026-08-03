package com.healthcare.clinic.identity.service;

import com.healthcare.clinic.identity.entity.AuditLog;
import com.healthcare.clinic.identity.entity.User;
import com.healthcare.clinic.identity.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(@Qualifier("identityAuditLogRepository") AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logAction(User user, String action, String entityType, String entityId, Map<String, Object> metadata) {
        AuditLog log = new AuditLog();
        log.setUser(user);
        log.setAction(action);
        log.setEntityType(entityType);
        log.setEntityId(entityId);
        log.setMetadata(metadata);
        
        auditLogRepository.save(log);
    }
}
