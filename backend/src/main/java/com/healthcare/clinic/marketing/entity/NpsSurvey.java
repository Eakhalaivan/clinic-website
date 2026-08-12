package com.healthcare.clinic.marketing.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.ZonedDateTime;

@Entity
@Table(name = "nps_surveys")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NpsSurvey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "appointment_id")
    private Long appointmentId;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @Column(name = "branch_id")
    private Long branchId;

    @Column(name = "service_id")
    private Long serviceId;

    @Column(name = "doctor_id")
    private Long doctorId;

    @Column(name = "sent_at")
    private ZonedDateTime sentAt;

    @Column(name = "completed_at")
    private ZonedDateTime completedAt;

    /**
     * Status: PENDING, SENT, COMPLETED, EXPIRED, SUPPRESSED
     */
    @Column(nullable = false, length = 30)
    @Builder.Default
    private String status = "PENDING";

    /**
     * Idempotency key to prevent duplicate surveys per event.
     * Format: "APPOINTMENT_{appointmentId}" or "ORDER_{orderId}"
     */
    @Column(name = "idempotency_key", nullable = false, length = 100, unique = true)
    private String idempotencyKey;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;
}
