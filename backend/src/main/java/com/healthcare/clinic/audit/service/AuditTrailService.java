package com.healthcare.clinic.audit.service;

import com.healthcare.clinic.audit.entity.AuditRecord;
import com.healthcare.clinic.audit.repository.AuditRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditTrailService {

    private final AuditRecordRepository auditRecordRepository;

    /**
     * Persists the audit record asynchronously to avoid blocking the main transaction.
     * Uses Propagation.REQUIRES_NEW to ensure it saves even if the parent transaction rolls back.
     */
    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logAuditAsync(AuditRecord record) {
        try {
            record.setEventId(UUID.randomUUID().toString());
            
            // 1. Fetch previous hash to maintain the chain
            AuditRecord lastRecord = auditRecordRepository.findTopByOrderByIdDesc().orElse(null);
            String prevHash = lastRecord != null ? lastRecord.getRecordHash() : "GENESIS";
            record.setPreviousHash(prevHash);

            // 2. Compute current hash
            String currentHash = computeHash(record);
            record.setRecordHash(currentHash);

            // 3. Save
            auditRecordRepository.save(record);
        } catch (Exception e) {
            log.error("CRITICAL: Failed to save audit record! Event: {}", record.getActionName(), e);
            // In a real system, you might write to an emergency file log here if DB is down.
        }
    }

    private String computeHash(AuditRecord record) throws NoSuchAlgorithmException {
        String payload = String.format("%s|%s|%s|%s|%s|%s|%s|%s|%s",
                record.getEventId(),
                record.getPreviousHash(),
                record.getActorId(),
                record.getActionName(),
                record.getResourceId(),
                record.getOutcome(),
                record.getBeforeValues(),
                record.getAfterValues(),
                record.getSensitivityLevel()
        );

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedhash = digest.digest(payload.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encodedhash);
    }
}
