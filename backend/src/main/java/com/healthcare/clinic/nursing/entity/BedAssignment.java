package com.healthcare.clinic.nursing.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Entity
@Table(name = "bed_assignments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BedAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bed_id", nullable = false)
    private Long bedId;

    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @Column(name = "encounter_id", nullable = false)
    private Long encounterId;

    @Column(name = "assigned_by", nullable = false)
    private Long assignedBy;

    @Column(name = "assigned_at")
    private ZonedDateTime assignedAt;

    @Column(name = "discharged_at")
    private ZonedDateTime dischargedAt;

    @Builder.Default
    @Column(length = 50)
    private String status = "ACTIVE"; // ACTIVE, TRANSFERRED, DISCHARGED

    @Column(columnDefinition = "TEXT")
    private String notes;

    @PrePersist
    protected void onCreate() {
        if (assignedAt == null) assignedAt = ZonedDateTime.now();
    }
}
