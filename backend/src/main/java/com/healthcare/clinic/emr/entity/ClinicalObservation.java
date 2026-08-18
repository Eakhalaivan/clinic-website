package com.healthcare.clinic.emr.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "clinical_observations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClinicalObservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @Column(name = "observation_code", length = 100)
    private String observationCode; // e.g., LOINC code

    @Column(name = "observation_name", nullable = false, length = 255)
    private String observationName;

    @Column(name = "observation_value", nullable = false, length = 255)
    private String value;

    @Column(length = 50)
    private String unit;

    @Column(name = "observed_at", nullable = false)
    private ZonedDateTime observedAt;

    @Column(name = "observed_by_user_id", nullable = false)
    private Long observedByUserId;

    @Column(name = "encounter_id")
    private Long encounterId;
}
