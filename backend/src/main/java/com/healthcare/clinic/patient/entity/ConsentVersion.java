package com.healthcare.clinic.patient.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.ZonedDateTime;

@Entity
@Table(name = "consent_versions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class ConsentVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50, unique = true)
    private String consentType; // e.g. "AI_ASSISTANT", "DATA_EXPORT", "TELECONSULTATION"

    @Column(nullable = false)
    private String versionId; // e.g. "v1.0.0"

    @Column(columnDefinition = "TEXT", nullable = false)
    private String documentText;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isLatest = true;

    @CreatedDate
    @Column(updatable = false)
    private ZonedDateTime createdAt;
}
