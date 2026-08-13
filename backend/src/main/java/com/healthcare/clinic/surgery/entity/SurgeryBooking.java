package com.healthcare.clinic.surgery.entity;

import com.healthcare.clinic.doctor.entity.DoctorProfile;
import com.healthcare.clinic.inpatient.entity.Admission;
import com.healthcare.clinic.patient.entity.PatientProfile;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.ZonedDateTime;

@Entity
@Table(name = "surgery_bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class SurgeryBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private PatientProfile patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "primary_surgeon_id", nullable = false)
    private DoctorProfile primarySurgeon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operation_theatre_id", nullable = false)
    private OperationTheatre operationTheatre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admission_id")
    private Admission admission; // Nullable for Day Care Surgeries

    @Column(name = "surgery_type", nullable = false, length = 100)
    private String surgeryType;

    @Column(name = "diagnosis", columnDefinition = "TEXT")
    private String diagnosis;

    @Column(name = "scheduled_start_time", nullable = false)
    private ZonedDateTime scheduledStartTime;

    @Column(name = "estimated_duration_minutes", nullable = false)
    private Integer estimatedDurationMinutes;

    @Column(nullable = false, length = 50)
    private String status; // SCHEDULED, IN_PROGRESS, COMPLETED, CANCELLED

    @Column(name = "actual_start_time")
    private ZonedDateTime actualStartTime;

    @Column(name = "actual_end_time")
    private ZonedDateTime actualEndTime;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @Version
    private Long version;
}
