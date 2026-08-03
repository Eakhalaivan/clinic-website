package com.healthcare.clinic.laboratory.entity;

import com.healthcare.clinic.identity.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;

@Entity
@Table(name = "lab_results")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LabResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id", nullable = false)
    private LabTestRequest request;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lab_tech_id", nullable = false)
    private User labTech;

    @Column(name = "result_value", columnDefinition = "TEXT", nullable = false)
    private String resultValue;

    @Column(name = "reference_range")
    private String referenceRange;

    @Column(length = 50)
    private String unit;

    @Column(name = "is_abnormal")
    @Builder.Default
    private Boolean isAbnormal = false;

    @Column(name = "entered_at", updatable = false)
    @Builder.Default
    private ZonedDateTime enteredAt = ZonedDateTime.now();

    @Column(name = "verified_at")
    private ZonedDateTime verifiedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "verified_by")
    private User verifiedBy;
}
