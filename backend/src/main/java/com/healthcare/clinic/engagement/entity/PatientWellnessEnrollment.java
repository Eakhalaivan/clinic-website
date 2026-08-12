package com.healthcare.clinic.engagement.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.ZonedDateTime;

@Entity
@Table(name = "patient_wellness_enrollments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class PatientWellnessEnrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long programId;

    @Column(nullable = false)
    private Long patientId;

    @CreatedDate
    @Column(updatable = false)
    private ZonedDateTime enrolledAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private EnrollmentStatus status = EnrollmentStatus.ACTIVE;

    @Column(columnDefinition = "TEXT")
    private String progressNotes;

    @LastModifiedDate
    private ZonedDateTime updatedAt;

    public enum EnrollmentStatus {
        ACTIVE, COMPLETED, DROPPED
    }
}
