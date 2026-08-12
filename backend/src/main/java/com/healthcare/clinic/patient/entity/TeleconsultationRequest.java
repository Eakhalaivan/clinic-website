package com.healthcare.clinic.patient.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;

@Entity
@Table(name = "teleconsultation_requests")
@Data
public class TeleconsultationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @Column(name = "preferred_dates", nullable = false)
    private String preferredDates;

    @Column(name = "preferred_times", nullable = false)
    private String preferredTimes;

    @Column(nullable = false)
    private String reason;

    @Column(name = "attached_document_url")
    private String attachedDocumentUrl;

    @Column(name = "language_preference", nullable = false)
    private String languagePreference = "English";

    @Column(nullable = false)
    private String status = "Requested";

    @Column(name = "assigned_doctor_id")
    private Long assignedDoctorId;

    @Column(name = "scheduled_time")
    private ZonedDateTime scheduledTime;

    @Column(name = "join_link")
    private String joinLink;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;
}
