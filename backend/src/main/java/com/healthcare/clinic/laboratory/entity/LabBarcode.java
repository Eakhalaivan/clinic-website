package com.healthcare.clinic.laboratory.entity;

import com.healthcare.clinic.identity.entity.User;
import com.healthcare.clinic.patient.entity.PatientProfile;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.ZonedDateTime;

@Entity
@Table(name = "lab_barcodes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LabBarcode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "barcode_value", unique = true, nullable = false, length = 50)
    private String barcodeValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private PatientProfile patient;

    @Column(name = "lab_request_number", nullable = false, length = 50)
    private String labRequestNumber;

    @Column(name = "specimen_type", nullable = false, length = 100)
    private String specimenType;

    @Column(name = "container_type", length = 100)
    private String containerType;

    @CreationTimestamp
    @Column(name = "generated_at", updatable = false)
    private ZonedDateTime generatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "generated_by")
    private User generatedBy;

    @Column(nullable = false, length = 50)
    @Builder.Default
    private String status = "PRINTED"; // PRINTED, SCANNED, REJECTED
}
