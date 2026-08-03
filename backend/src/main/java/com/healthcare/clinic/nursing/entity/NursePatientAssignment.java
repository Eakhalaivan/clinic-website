package com.healthcare.clinic.nursing.entity;

import com.healthcare.clinic.identity.entity.User;
import com.healthcare.clinic.patient.entity.PatientProfile;
import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;

@Entity
@Table(name = "nurse_patient_assignment")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NursePatientAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nurse_id", nullable = false)
    private User nurse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private PatientProfile patient;

    @Column(name = "assigned_at", updatable = false)
    @Builder.Default
    private ZonedDateTime assignedAt = ZonedDateTime.now();

    @Column(length = 50)
    @Builder.Default
    private String status = "ACTIVE";
}
