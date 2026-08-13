package com.healthcare.clinic.audit.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.ZonedDateTime;

@Entity
@Table(name = "compliance_audit_logs", indexes = {
        @Index(name = "idx_audit_patient", columnList = "patient_id"),
        @Index(name = "idx_audit_user", columnList = "actor_id"),
        @Index(name = "idx_audit_module", columnList = "module_name"),
        @Index(name = "idx_audit_action", columnList = "action_name"),
        @Index(name = "idx_audit_created_at", columnList = "created_at")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class AuditRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id", unique = true, nullable = false, length = 100)
    private String eventId;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    // Actor Details
    @Column(name = "actor_id")
    private Long actorId;
    
    @Column(name = "actor_role", length = 100)
    private String actorRole;
    
    @Column(name = "actor_type", length = 50)
    private String actorType; // HUMAN, SYSTEM, INTEGRATION

    // Context Details
    @Column(name = "tenant_id")
    private Long tenantId; // or Branch ID
    
    @Column(name = "module_name", length = 100)
    private String moduleName;
    
    @Column(name = "action_name", nullable = false, length = 100)
    private String actionName;

    // Resource Details
    @Column(name = "resource_type", length = 100)
    private String resourceType;
    
    @Column(name = "resource_id", length = 100)
    private String resourceId;
    
    @Column(name = "patient_id")
    private Long patientId;
    
    @Column(name = "reference_id", length = 100)
    private String referenceId; // Encounter / Visit / Invoice / Lab ID

    // Data changes
    @Column(name = "before_values", columnDefinition = "TEXT")
    private String beforeValues;
    
    @Column(name = "after_values", columnDefinition = "TEXT")
    private String afterValues;

    // Execution Details
    @Column(nullable = false, length = 50)
    private String outcome; // SUCCESS, DENIED, FAILED
    
    @Column(columnDefinition = "TEXT")
    private String reason; // Justification for break-glass or failure reason

    // Network & Session
    @Column(name = "session_id", length = 100)
    private String sessionId;
    
    @Column(name = "ip_address", length = 50)
    private String ipAddress;
    
    @Column(name = "user_agent", length = 255)
    private String userAgent;
    
    @Column(name = "source_channel", length = 50)
    private String sourceChannel; // WEB, MOBILE, API, BACKGROUND

    // Compliance Flags
    @Column(name = "sensitivity_level", length = 50)
    private String sensitivityLevel; // NORMAL, HIGH, PHI_RESTRICTED
    
    @Column(name = "break_glass_used")
    @Builder.Default
    private Boolean breakGlassUsed = false;

    // Integrity
    @Column(name = "previous_hash", length = 128)
    private String previousHash; // For chain integrity
    
    @Column(name = "record_hash", nullable = false, length = 128)
    private String recordHash;
}
