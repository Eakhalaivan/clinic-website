package com.healthcare.clinic.ai.service;

import com.healthcare.clinic.ai.entity.AiAuditLog;
import com.healthcare.clinic.ai.repository.AiAuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AiDoctorService {
    
    private final AiAuditLogRepository auditRepository;

    public String generateSummary(Long encounterId, Long doctorId, Long tenantId) {
        String summary = "This is an AI-generated summary of Encounter #" + encounterId + ". " +
                         "Please review carefully before attaching to the permanent record.";
                         
        AiAuditLog log = AiAuditLog.builder()
            .userId(doctorId)
            .tenantId(tenantId)
            .userRole("DOCTOR")
            .actionType("PROMPT_GENERATED")
            .payload("Generated summary for encounter " + encounterId)
            .build();
        auditRepository.save(log);
        
        return summary;
    }
    
    public void approveSummary(Long encounterId, Long doctorId, Long tenantId) {
        AiAuditLog log = AiAuditLog.builder()
            .userId(doctorId)
            .tenantId(tenantId)
            .userRole("DOCTOR")
            .actionType("SUMMARY_ACCEPTED")
            .payload("Doctor approved summary for encounter " + encounterId)
            .build();
        auditRepository.save(log);
    }
}
