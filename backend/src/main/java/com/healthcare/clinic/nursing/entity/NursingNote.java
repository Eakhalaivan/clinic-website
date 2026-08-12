package com.healthcare.clinic.nursing.entity;

import com.healthcare.clinic.identity.entity.User;
import com.healthcare.clinic.patient.entity.PatientProfile;
import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;

@Entity
@Table(name = "nursing_notes")
@EntityListeners(org.springframework.data.jpa.domain.support.AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NursingNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private PatientProfile patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nurse_id", nullable = false)
    private User nurse;

    @Column(name = "encounter_id")
    private Long encounterId;

    @Column(name = "note_type")
    private String noteType;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String note;

    @org.springframework.data.annotation.CreatedDate
    @Column(name = "recorded_at", updatable = false)
    @Builder.Default
    private ZonedDateTime recordedAt = ZonedDateTime.now();

    @org.springframework.data.annotation.LastModifiedDate
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    @Version
    private Integer version;
}
