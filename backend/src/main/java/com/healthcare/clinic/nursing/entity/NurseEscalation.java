package com.healthcare.clinic.nursing.entity;

import com.healthcare.clinic.identity.entity.User;
import com.healthcare.clinic.patient.entity.PatientProfile;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.ZonedDateTime;

@Entity
@Table(name = "nurse_escalations")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NurseEscalation {

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private User doctor;

    @Column(nullable = false)
    private String reason;

    @Column(name = "clinical_context", columnDefinition = "TEXT", nullable = false)
    private String clinicalContext;

    @Column(nullable = false)
    private String priority;

    @Column(nullable = false)
    @Builder.Default
    private String status = "OPEN";

    @CreatedDate
    @Column(name = "escalated_at", updatable = false)
    private ZonedDateTime escalatedAt;

    @Column(name = "resolved_at")
    private ZonedDateTime resolvedAt;

    @Column(name = "resolution_notes", columnDefinition = "TEXT")
    private String resolutionNotes;
}
