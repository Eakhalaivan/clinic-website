package com.healthcare.clinic.marketing.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Map;

@Entity
@Table(name = "patient_memberships")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientMembership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @Column(name = "plan_id", nullable = false)
    private Long planId;

    /**
     * Status: ACTIVE, EXPIRING, RENEWED, PAUSED, CANCELLED, EXPIRED
     */
    @Column(nullable = false, length = 30)
    @Builder.Default
    private String status = "ACTIVE";

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "activated_by")
    private Long activatedBy;

    @Column(name = "cancelled_at")
    private ZonedDateTime cancelledAt;

    @Column(name = "cancel_reason")
    private String cancelReason;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "usage_summary")
    private Map<String, Object> usageSummary;

    @Column(name = "renewed_from_id")
    private Long renewedFromId;

    @Column(name = "invoice_id")
    private Long invoiceId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;
}
