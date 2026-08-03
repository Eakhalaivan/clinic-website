package com.healthcare.clinic.doctor.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Table(name = "doctor_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class DoctorProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long userId; // Maps to identity-service users table

    @jakarta.validation.constraints.NotBlank
    @Column(nullable = false, length = 100)
    private String specialty;

    @jakarta.validation.constraints.NotBlank
    @Column(nullable = false, columnDefinition = "TEXT")
    private String qualifications;

    private Integer experienceYears;

    @jakarta.validation.constraints.NotNull
    @jakarta.validation.constraints.Min(0)
    @Column(nullable = false)
    private BigDecimal consultationFee;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(name = "registration_number", length = 100)
    private String registrationNumber;

    @Builder.Default
    private Boolean isActive = true;

    @Column(nullable = false)
    private Long branchId;

    @CreatedDate
    @Column(updatable = false)
    private ZonedDateTime createdAt;

    @LastModifiedDate
    private ZonedDateTime updatedAt;
}
