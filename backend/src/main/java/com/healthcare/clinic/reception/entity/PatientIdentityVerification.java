package com.healthcare.clinic.reception.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.ZonedDateTime;

@Entity
@Table(name = "patient_identity_verifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class PatientIdentityVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @Column(name = "verification_method", nullable = false, length = 50)
    private String verificationMethod;

    @Column(name = "verified_by_user_id", nullable = false)
    private Long verifiedByUserId;

    @CreatedDate
    @Column(name = "verified_at", updatable = false)
    private ZonedDateTime verifiedAt;

    @Column(nullable = false, length = 50)
    private String status;

    @Column(name = "failure_reason")
    private String failureReason;

    @Column(name = "document_reference")
    private String documentReference;
}
