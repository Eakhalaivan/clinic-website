package com.healthcare.clinic.doctor.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.ZonedDateTime;

@Entity
@Table(name = "clinical_messages")
@Data
public class ClinicalMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sender_id", nullable = false)
    private Long senderId;

    @Column(name = "recipient_id", nullable = false)
    private Long recipientId;

    @Column(name = "patient_id")
    private Long patientId;

    @Column(name = "encounter_id")
    private Long encounterId;

    @Column(name = "subject")
    private String subject;

    @Column(name = "body", nullable = false)
    private String body;

    @Column(name = "priority")
    private String priority = "Normal";

    @Column(name = "is_read")
    private Boolean isRead = false;

    @Column(name = "read_at")
    private ZonedDateTime readAt;

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = ZonedDateTime.now();
    }
}
