package com.healthcare.clinic.emr.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.ZonedDateTime;

@Entity
@Table(name = "problems")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Problem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @Column(name = "icd10_code", length = 20)
    private String icd10Code;

    @Column(name = "problem_name", nullable = false, length = 255)
    private String problemName;

    @Column(nullable = false, length = 50)
    private String status; // ACTIVE, RESOLVED, CHRONIC

    @Column(name = "onset_date")
    private LocalDate onsetDate;

    @Column(name = "resolved_date")
    private LocalDate resolvedDate;

    @Column(name = "recorded_by_user_id", nullable = false)
    private Long recordedByUserId;

    @Column(name = "recorded_at", nullable = false)
    private ZonedDateTime recordedAt;

    @Column(name = "source_encounter_id")
    private Long sourceEncounterId;
}
