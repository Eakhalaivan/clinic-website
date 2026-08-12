package com.healthcare.clinic.inpatient.entity;

import com.healthcare.clinic.doctor.entity.DoctorProfile;
import com.healthcare.clinic.patient.entity.PatientProfile;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.ZonedDateTime;

@Entity
@Table(name = "admissions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Admission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "admission_number", unique = true, nullable = false, length = 50)
    private String admissionNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private PatientProfile patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admitting_doctor_id", nullable = false)
    private DoctorProfile admittingDoctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bed_id", nullable = false)
    private Bed bed;

    @Column(name = "admission_type", nullable = false, length = 50)
    private String admissionType; // PLANNED, EMERGENCY, TRANSFER_IN

    @Column(name = "admitted_at", nullable = false, updatable = false)
    @Builder.Default
    private ZonedDateTime admittedAt = ZonedDateTime.now();

    @Column(nullable = false, length = 50)
    private String status; // ADMITTED, DISCHARGED, TRANSFERRED_OUT, DECEASED

    @Column(name = "discharged_at")
    private ZonedDateTime dischargedAt;

    @Column(name = "admission_reason", columnDefinition = "TEXT")
    private String admissionReason;

    @Column(name = "branch_id", nullable = false)
    private Long branchId;
}
