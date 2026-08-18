package com.healthcare.clinic.doctor.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "prescription_reconciliation_mismatches")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionReconciliationMismatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "clinical_prescription_id", nullable = false)
    private Long clinicalPrescriptionId;

    @Column(name = "clinic_status", length = 50)
    private String clinicStatus;

    @Column(name = "pharmacy_status", length = 50)
    private String pharmacyStatus;

    @Column(name = "mismatch_details", columnDefinition = "TEXT")
    private String mismatchDetails;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder.Default
    @Column(name = "resolved")
    private Boolean resolved = false;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @Column(name = "resolved_by")
    private String resolvedBy;
}
