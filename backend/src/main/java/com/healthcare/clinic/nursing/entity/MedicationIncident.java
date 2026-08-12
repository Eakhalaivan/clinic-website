package com.healthcare.clinic.nursing.entity;

import com.healthcare.clinic.identity.entity.User;
import com.healthcare.clinic.patient.entity.PatientProfile;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.ZonedDateTime;

@Entity
@Table(name = "medication_incidents")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicationIncident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private PatientProfile patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nurse_id", nullable = false)
    private User nurse;

    @Column(name = "medication_name", nullable = false)
    private String medicationName;

    @Column(name = "incident_type", nullable = false)
    private String incidentType;

    @Column(name = "incident_time", nullable = false)
    private ZonedDateTime incidentTime;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "action_taken", columnDefinition = "TEXT")
    private String actionTaken;

    @Column(name = "doctor_notified")
    @Builder.Default
    private Boolean doctorNotified = false;

    @CreatedDate
    @Column(name = "reported_at", updatable = false)
    private ZonedDateTime reportedAt;

    @Column(nullable = false)
    @Builder.Default
    private String status = "OPEN";
}
