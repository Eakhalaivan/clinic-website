package com.healthcare.clinic.nursing.entity;

import com.healthcare.clinic.identity.entity.User;
import com.healthcare.clinic.patient.entity.PatientProfile;
import com.healthcare.clinic.appointment.entity.Appointment;
import com.healthcare.clinic.inpatient.entity.Admission;
import com.healthcare.clinic.surgery.entity.SurgeryBooking;
import com.healthcare.clinic.emergency.entity.EmergencyEncounter;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Table(name = "vital_signs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VitalSign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @com.fasterxml.jackson.annotation.JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private PatientProfile patient;

    @com.fasterxml.jackson.annotation.JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nurse_id", nullable = false)
    private User nurse;

    @com.fasterxml.jackson.annotation.JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admission_id")
    private Admission admission;

    // These don't exist yet but we add columns to support them when they are created later.
    // To avoid compilation errors, we can map them by just column ID for now if needed.
    // Let me add them as Longs if the entities don't exist yet, or I can just create the entities now.
    @Column(name = "emergency_encounter_id")
    private Long emergencyEncounterId;

    @Column(name = "surgery_booking_id")
    private Long surgeryBookingId;

    @Column(precision = 5, scale = 2)
    private BigDecimal temperature;

    @Column(name = "blood_pressure", length = 20)
    private String bloodPressure;

    @Column(name = "heart_rate")
    private Integer heartRate;

    @Column(name = "respiratory_rate")
    private Integer respiratoryRate;

    @Column(name = "oxygen_saturation", precision = 5, scale = 2)
    private BigDecimal oxygenSaturation;

    @Column(name = "recorded_at", updatable = false)
    @Builder.Default
    private ZonedDateTime recordedAt = ZonedDateTime.now();

    @Column(columnDefinition = "TEXT")
    private String notes;
}
