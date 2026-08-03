package com.healthcare.clinic.clinicaldecision.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.ZonedDateTime;

@Entity
@Table(name = "cds_alerts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CdsAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @Column(name = "rule_id")
    private Long ruleId;

    @Column(name = "triggered_by_user_id")
    private Long triggeredByUserId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private Severity severity = Severity.WARNING;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Builder.Default
    private AlertStatus status = AlertStatus.PENDING;

    @Column(name = "override_reason", columnDefinition = "TEXT")
    private String overrideReason;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @Column(name = "acknowledged_at")
    private ZonedDateTime acknowledgedAt;
}
