package com.healthcare.clinic.emr.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "family_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FamilyHistoryEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @Column(nullable = false, length = 50)
    private String relationship;

    @Column(nullable = false, length = 255)
    private String condition;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "recorded_by_user_id", nullable = false)
    private Long recordedByUserId;

    @Column(name = "recorded_at", nullable = false)
    private ZonedDateTime recordedAt;
}
