package com.healthcare.clinic.patient.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.ZonedDateTime;

@Entity
@Table(name = "patient_documents")
@Data
public class PatientDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @Column(nullable = false)
    private String title;

    @Column(name = "document_type", nullable = false)
    private String documentType; // e.g. "Lab Report", "Prescription", "Medical Record", "Other"

    @Column(name = "file_url", nullable = false)
    private String fileUrl;

    @Column(name = "storage_key")
    private String storageKey;

    @CreationTimestamp
    @Column(name = "uploaded_at", updatable = false)
    private ZonedDateTime uploadedAt;
}
