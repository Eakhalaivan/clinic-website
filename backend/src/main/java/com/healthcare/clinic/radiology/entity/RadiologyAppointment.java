package com.healthcare.clinic.radiology.entity;

import com.healthcare.clinic.branch.entity.Branch;
import com.healthcare.clinic.identity.entity.User;
import com.healthcare.clinic.patient.entity.PatientProfile;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;

@Entity
@Table(name = "radiology_appointments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RadiologyAppointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id", nullable = false)
    private ImagingRequest request;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private PatientProfile patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @Column(name = "modality", nullable = false, length = 30)
    private String modality;

    @Column(name = "scheduled_time", nullable = false)
    private ZonedDateTime scheduledTime;

    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "technician_id")
    private User technician;

    @Column(name = "room_or_machine", length = 100)
    private String roomOrMachine;

    @Column(nullable = false, length = 30)
    @Builder.Default
    private String status = "SCHEDULED"; // SCHEDULED, CHECKED_IN, COMPLETED, CANCELLED, NO_SHOW

    @Column(name = "cancellation_reason", length = 255)
    private String cancellationReason;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;
}
