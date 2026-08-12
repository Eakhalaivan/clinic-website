package com.healthcare.clinic.inpatient.entity;

import com.healthcare.clinic.appointment.entity.Appointment;
import com.healthcare.clinic.doctor.entity.DoctorProfile;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.ZonedDateTime;

@Entity
@Table(name = "discharge_summaries")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DischargeSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admission_id", nullable = false, unique = true)
    private Admission admission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discharging_doctor_id", nullable = false)
    private DoctorProfile dischargingDoctor;

    @Column(columnDefinition = "TEXT")
    private String diagnosis;

    @Column(name = "treatment_summary", columnDefinition = "TEXT")
    private String treatmentSummary;

    @Column(name = "medications_on_discharge", columnDefinition = "TEXT")
    private String medicationsOnDischarge;

    @Column(name = "follow_up_instructions", columnDefinition = "TEXT")
    private String followUpInstructions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follow_up_appointment_id")
    private Appointment followUpAppointment;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;
}
