package com.healthcare.clinic.emr.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.ZonedDateTime;

@Entity
@Table(name = "external_medications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExternalMedicationHistoryEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @Column(name = "medication_name", nullable = false, length = 255)
    private String medicationName;

    @Column(length = 100)
    private String dosage;

    @Column(length = 100)
    private String frequency;

    @Column(name = "started_date")
    private LocalDate startedDate;

    @Column(name = "still_taking", nullable = false)
    @Builder.Default
    private Boolean stillTaking = true;

    @Column(name = "recorded_by_user_id", nullable = false)
    private Long recordedByUserId;

    @Column(name = "recorded_at", nullable = false)
    private ZonedDateTime recordedAt;
}
