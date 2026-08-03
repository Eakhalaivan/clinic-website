package com.healthcare.clinic.insurance.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Table(name = "insurance_pre_authorizations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InsurancePreAuth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @Column(name = "provider_name", nullable = false, length = 200)
    private String providerName;

    @Column(name = "policy_number", length = 100)
    private String policyNumber;

    @Column(name = "procedure_name", nullable = false)
    private String procedureName;

    @Column(name = "estimated_cost", nullable = false, precision = 12, scale = 2)
    private BigDecimal estimatedCost;

    @Column(name = "approved_amount", precision = 12, scale = 2)
    private BigDecimal approvedAmount;

    @Column(nullable = false, length = 30)
    @Builder.Default
    private String status = "SUBMITTED"; // SUBMITTED, APPROVED, REJECTED, PENDING_INFO

    @Column(name = "denial_reason", columnDefinition = "TEXT")
    private String denialReason;

    @CreationTimestamp
    @Column(name = "submitted_at", nullable = false, updatable = false)
    private ZonedDateTime submittedAt;

    @Column(name = "adjudicated_at")
    private ZonedDateTime adjudicatedAt;
}
