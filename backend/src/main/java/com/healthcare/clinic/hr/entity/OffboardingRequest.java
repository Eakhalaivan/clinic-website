package com.healthcare.clinic.hr.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.time.ZonedDateTime;

@Entity
@Table(name = "offboarding_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OffboardingRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(nullable = false, length = 50)
    private String reason; // RESIGNATION, TERMINATION, RETIREMENT, CONTRACT_COMPLETION

    @Column(name = "last_working_day", nullable = false)
    private LocalDate lastWorkingDay;

    @Column(nullable = false, length = 30)
    @Builder.Default
    private String status = "INITIATED"; // INITIATED, NOTICE_PERIOD, CLEARANCE_PENDING, FINAL_SETTLEMENT, ACCESS_REVOKED, COMPLETED

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "clearance_checklist")
    @Builder.Default
    private String clearanceChecklist = "[]";

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;
}
