package com.healthcare.clinic.radiology.entity;

import com.healthcare.clinic.doctor.entity.DoctorProfile;
import com.healthcare.clinic.patient.entity.PatientProfile;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.ZonedDateTime;

@Entity
@Table(name = "imaging_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImagingRequest {

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
    @JoinColumn(name = "procedure_id", nullable = false)
    private ImagingProcedure procedure;

    @Column(nullable = false, length = 20)
    @Builder.Default
    private String priority = "ROUTINE"; // ROUTINE, URGENT, STAT

    @Column(name = "clinical_notes", columnDefinition = "TEXT")
    private String clinicalNotes;

    @Column(nullable = false, length = 30)
    @Builder.Default
    private String status = "DRAFT"; // DRAFT, ORDERED, AWAITING_PAYMENT, SCHEDULED, CHECKED_IN, ACQUIRED, REPORTING, VERIFIED, RELEASED, AMENDED, CANCELLED, REJECTED

    @Column(name = "turnaround_target_sla")
    private ZonedDateTime turnaroundTargetSla;

    @CreationTimestamp
    @Column(name = "requested_at", nullable = false, updatable = false)
    private ZonedDateTime requestedAt;

    @Column(name = "scheduled_at")
    private ZonedDateTime scheduledAt;

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
