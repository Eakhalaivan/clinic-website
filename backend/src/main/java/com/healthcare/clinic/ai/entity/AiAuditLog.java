package com.healthcare.clinic.ai.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Filter;
import java.time.LocalDateTime;

@Entity
@Table(name = "ai_audit_logs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
public class AiAuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id")
    private Long tenantId;

    @Column(nullable = false)
    private Long userId;
    
    private String userRole;
    private String actionType; // PROMPT_GENERATED, SUMMARY_ACCEPTED, SAFETY_ESCALATION

    @Column(columnDefinition = "TEXT")
    private String payload;
    
    private String modelVersion;
    private Long processingTimeMs;

    @CreationTimestamp
    private LocalDateTime recordedAt;
}
