package com.healthcare.clinic.emr.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "social_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SocialHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_id", nullable = false, unique = true)
    private Long patientId;

    @Column(name = "smoking_status", length = 100)
    private String smokingStatus;

    @Column(name = "alcohol_use", length = 100)
    private String alcoholUse;

    @Column(length = 100)
    private String occupation;

    @Column(name = "exercise_frequency", length = 100)
    private String exerciseFrequency;

    @Column(name = "other_notes", columnDefinition = "TEXT")
    private String otherNotes;

    @Column(name = "updated_by_user_id", nullable = false)
    private Long updatedByUserId;

    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;
}
