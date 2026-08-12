package com.healthcare.clinic.doctor.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Table(name = "billing_outbox")
@Data
public class BillingOutbox {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "encounter_id", nullable = false)
    private Long encounterId;

    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @Column(name = "doctor_id", nullable = false)
    private Long doctorId;

    @Column(name = "service_type", nullable = false)
    private String serviceType;

    @Column(name = "service_code")
    private String serviceCode;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "status")
    private String status = "Pending";

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @Column(name = "processed_at")
    private ZonedDateTime processedAt;

    @Column(name = "error_message")
    private String errorMessage;

    @PrePersist
    protected void onCreate() {
        createdAt = ZonedDateTime.now();
    }
}
