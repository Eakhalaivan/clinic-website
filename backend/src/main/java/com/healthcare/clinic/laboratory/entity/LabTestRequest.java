package com.healthcare.clinic.laboratory.entity;

import com.healthcare.clinic.doctor.entity.DoctorProfile;
import com.healthcare.clinic.identity.entity.User;
import com.healthcare.clinic.patient.entity.PatientProfile;
import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;

@Entity
@Table(name = "lab_test_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LabTestRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private PatientProfile patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private DoctorProfile doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_catalog_id", nullable = false)
    private LabTestCatalog testCatalog;

    @Column(nullable = false, length = 50)
    @Builder.Default
    private String status = "REQUESTED";

    @Column(name = "requested_at", updatable = false)
    @Builder.Default
    private ZonedDateTime requestedAt = ZonedDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "rejection_reason")
    private RejectionReason rejectionReason;

    @Column(name = "rejection_notes")
    private String rejectionNotes;

    @Column(name = "rejected_at")
    private ZonedDateTime rejectedAt;
    
    @Column(name = "released_at")
    private ZonedDateTime releasedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accepted_by_id")
    private User acceptedBy;

    @Column(name = "sample_collected_at")
    private ZonedDateTime sampleCollectedAt;

    @Column(name = "scheduled_at")
    private ZonedDateTime scheduledAt;

    @Column(length = 50)
    @Builder.Default
    private String priority = "ROUTINE";

    @Column(name = "accepted_at")
    private ZonedDateTime acceptedAt;

    @Column(name = "sample_barcode_id", unique = true, length = 50)
    private String sampleBarcodeId;

    @Column(name = "lab_request_number", unique = true, length = 50)
    private String labRequestNumber;

    @OneToOne(mappedBy = "request", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private LabSampleCollection sampleCollection;

    @OneToOne(mappedBy = "request", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private LabProcessingDetails processingDetails;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "encounter_id")
    private com.healthcare.clinic.medicalrecord.entity.MedicalRecord encounter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id")
    private com.healthcare.clinic.branch.entity.Branch branch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id")
    private com.healthcare.clinic.billing.entity.Invoice invoice;
}
