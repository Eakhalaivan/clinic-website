package com.healthcare.clinic.laboratory.entity;

import com.healthcare.clinic.doctor.entity.DoctorProfile;
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

    @Column(name = "sample_collected_at")
    private ZonedDateTime sampleCollectedAt;

    @Column(length = 50)
    @Builder.Default
    private String priority = "ROUTINE";
}
