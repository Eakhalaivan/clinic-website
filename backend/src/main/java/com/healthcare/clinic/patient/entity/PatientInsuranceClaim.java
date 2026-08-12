package com.healthcare.clinic.patient.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Table(name = "patient_insurance_claims")
@Data
public class PatientInsuranceClaim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @Column(nullable = false)
    private String provider;

    @Column(name = "policy_number", nullable = false)
    private String policyNumber;

    @Column(name = "claim_amount", nullable = false)
    private BigDecimal claimAmount;

    @Column(nullable = false)
    private String status = "Submitted";

    @Column(columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    @Column(name = "submitted_at", updatable = false)
    private ZonedDateTime submittedAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;
}
