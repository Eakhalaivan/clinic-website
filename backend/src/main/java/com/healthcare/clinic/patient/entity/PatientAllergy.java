package com.healthcare.clinic.patient.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.ZonedDateTime;

@Entity
@Table(name = "patient_allergies")
@Data
public class PatientAllergy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @Column(nullable = false)
    private String allergen;

    @Column(name = "allergy_type", nullable = false)
    private String allergyType; // Drug, Food, Environmental

    @Column
    private String reaction;

    @Column
    private String severity; // Mild, Moderate, Severe, Critical

    @Column
    private LocalDate onset;

    @Column(nullable = false)
    private String status = "Active"; // Active, Inactive, Entered_in_Error

    @Column(name = "verification_status", nullable = false)
    private String verificationStatus = "Unverified"; // Unverified, Verified, Patient_Reported

    @Column
    private String source;

    @Column(name = "recorded_by", nullable = false)
    private Long recordedBy;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;
}
