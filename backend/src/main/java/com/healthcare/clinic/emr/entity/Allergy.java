package com.healthcare.clinic.emr.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "allergies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Allergy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @Column(nullable = false, length = 255)
    private String allergen;

    @Column(name = "allergy_type", nullable = false, length = 50)
    private String allergyType; // DRUG, FOOD, ENVIRONMENTAL

    @Column(name = "reaction_severity", nullable = false, length = 50)
    private String reactionSeverity; // MILD, MODERATE, SEVERE, LIFE_THREATENING

    @Column(name = "reaction_description", columnDefinition = "TEXT")
    private String reactionDescription;

    @Column(nullable = false, length = 50)
    private String status; // ACTIVE, INACTIVE

    @Column(name = "recorded_by_user_id", nullable = false)
    private Long recordedByUserId;

    @Column(name = "recorded_at", nullable = false)
    private ZonedDateTime recordedAt;
}
