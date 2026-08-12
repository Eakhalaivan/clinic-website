package com.healthcare.clinic.reception.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.ZonedDateTime;

@Entity
@Table(name = "reception_document_uploads")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceptionDocumentUpload {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_document_id", nullable = false)
    private Long patientDocumentId;

    @Column(name = "uploaded_by_staff_id")
    private Long uploadedByStaffId;

    @Column(name = "branch_id", nullable = false)
    private Long branchId;

    @Column(name = "scan_device")
    private String scanDevice;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt;
}
