package com.healthcare.clinic.reception.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Entity
@Table(name = "no_shows")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoShow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @Column(name = "appointment_id")
    private Long appointmentId;

    @Column(name = "walk_in_id")
    private Long walkInId;

    @Column(name = "recorded_at", nullable = false, updatable = false)
    @Builder.Default
    private ZonedDateTime recordedAt = ZonedDateTime.now();

    @Column(name = "recorded_by_user_id")
    private Long recordedByUserId;

    @Column(name = "reason")
    private String reason;
}
