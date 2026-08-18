package com.healthcare.clinic.patient.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.ZonedDateTime;

@Entity
@Table(name = "patient_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class PatientProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long userId; // Maps to identity-service users table

    private LocalDate dateOfBirth;

    @Column(length = 10)
    private String gender;

    @Column(length = 5)
    private String bloodGroup;

    @Column(length = 100)
    private String emergencyContactName;

    @Column(length = 20)
    private String emergencyContactPhone;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(columnDefinition = "TEXT")
    private String medicalHistorySummary;

    @JdbcTypeCode(SqlTypes.JSON)
    
    @Builder.Default
    private String allergies = "[]";

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "chronic_conditions")
    @Builder.Default
    private String chronicConditions = "[]";

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "past_surgeries")
    @Builder.Default
    private String pastSurgeries = "[]";

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "family_history")
    @Builder.Default
    private String familyHistory = "[]";

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "current_medications")
    @Builder.Default
    private String currentMedications = "[]";

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "documents")
    @Builder.Default
    private String documents = "[]";

    @Column(nullable = false)
    private Long branchId;

    @Column(name = "tenant_id")
    private Long tenantId;

    @Column(name = "op_number", length = 50, unique = true)
    private String opNumber;

    @Column(name = "is_duplicate_of")
    private Long isDuplicateOf;

    @Column(name = "merge_reason")
    private String mergeReason;

    @Column(name = "preferred_communication", length = 20)
    @Builder.Default
    private String preferredCommunication = "EMAIL";

    @CreatedDate
    @Column(updatable = false)
    private ZonedDateTime createdAt;

    @LastModifiedDate
    private ZonedDateTime updatedAt;

    @Column(name = "insurance_status", length = 50)
    private String insuranceStatus;

    @Column(name = "injury_status", length = 50)
    private String injuryStatus;
}
