package com.healthcare.clinic.doctor.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.ZonedDateTime;

@Entity
@Table(name = "clinical_referrals")
@Data
public class ClinicalReferral {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @Column(name = "encounter_id")
    private Long encounterId;

    @Column(name = "referring_doctor_id", nullable = false)
    private Long referringDoctorId;

    @Column(name = "referred_to_specialty")
    private String referredToSpecialty;

    @Column(name = "referred_to_doctor_id")
    private Long referredToDoctorId;

    @Column(name = "referred_to_facility")
    private String referredToFacility;

    @Column(name = "reason_for_referral", nullable = false)
    private String reasonForReferral;

    @Column(name = "clinical_notes")
    private String clinicalNotes;

    @Column(name = "priority")
    private String priority = "Routine";

    @Column(name = "status")
    private String status = "Draft"; // Draft, Sent, Accepted, Completed, Cancelled

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = ZonedDateTime.now();
        updatedAt = ZonedDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = ZonedDateTime.now();
    }
}
