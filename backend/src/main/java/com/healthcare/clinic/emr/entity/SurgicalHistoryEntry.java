package com.healthcare.clinic.emr.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.ZonedDateTime;

@Entity
@Table(name = "surgical_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurgicalHistoryEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @Column(name = "procedure_name", nullable = false, length = 255)
    private String procedureName;

    @Column(name = "surgery_date")
    private LocalDate surgeryDate;

    @Column(length = 255)
    private String surgeon;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "surgery_booking_id")
    private Long surgeryBookingId; // Link to OT module if performed in-house

    @Column(name = "recorded_by_user_id", nullable = false)
    private Long recordedByUserId;

    @Column(name = "recorded_at", nullable = false)
    private ZonedDateTime recordedAt;
}
