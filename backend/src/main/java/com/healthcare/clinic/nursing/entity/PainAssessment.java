package com.healthcare.clinic.nursing.entity;

import com.healthcare.clinic.identity.entity.User;
import com.healthcare.clinic.patient.entity.PatientProfile;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.ZonedDateTime;

@Entity
@Table(name = "pain_assessments")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PainAssessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private PatientProfile patient;

    @Column(name = "encounter_id")
    private Long encounterId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nurse_id", nullable = false)
    private User nurse;

    @Column(name = "pain_score", nullable = false)
    private Integer painScore;

    @Column(name = "pain_location")
    private String painLocation;

    @Column(name = "pain_characteristics")
    private String painCharacteristics;

    @Column(columnDefinition = "TEXT")
    private String interventions;

    @Column(name = "reassessment_time")
    private ZonedDateTime reassessmentTime;

    @CreatedDate
    @Column(name = "assessed_at", updatable = false)
    private ZonedDateTime assessedAt;
}
