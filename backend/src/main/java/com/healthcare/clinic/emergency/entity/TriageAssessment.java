package com.healthcare.clinic.emergency.entity;

import com.healthcare.clinic.identity.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.ZonedDateTime;

@Entity
@Table(name = "triage_assessments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class TriageAssessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emergency_encounter_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private EmergencyEncounter emergencyEncounter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "triaged_by_user_id", nullable = false)
    private User triagedBy;

    @Column(name = "triage_level", nullable = false, length = 50)
    private String triageLevel; // CRITICAL, URGENT, SEMI_URGENT, NON_URGENT

    @Column(name = "chief_complaint", columnDefinition = "TEXT")
    private String chiefComplaint;

    @CreatedDate
    @Column(name = "triaged_at", nullable = false, updatable = false)
    private ZonedDateTime triagedAt;
}
