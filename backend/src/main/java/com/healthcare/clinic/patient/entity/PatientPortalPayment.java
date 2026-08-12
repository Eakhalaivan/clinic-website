package com.healthcare.clinic.patient.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Table(name = "patient_portal_payments")
@Data
public class PatientPortalPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(name = "payment_method", nullable = false)
    private String paymentMethod;

    @Column(nullable = false)
    private String status = "Pending";

    @Column(name = "transaction_id")
    private String transactionId;

    @CreationTimestamp
    @Column(name = "payment_date", updatable = false)
    private ZonedDateTime paymentDate;
}
