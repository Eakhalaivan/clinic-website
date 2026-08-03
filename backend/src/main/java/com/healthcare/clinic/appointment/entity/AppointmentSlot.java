package com.healthcare.clinic.appointment.entity;

import com.healthcare.clinic.doctor.entity.DoctorProfile;

import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;

@Entity
@Table(name = "appointment_slots")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private DoctorProfile doctor;

    @Column(nullable = false)
    private ZonedDateTime startTime;

    @Column(nullable = false)
    private ZonedDateTime endTime;

    @Builder.Default
    private Boolean isBooked = false;

    @Column(nullable = false)
    private Long branchId;

    @Version
    private Long version; // Optimistic locking
}
