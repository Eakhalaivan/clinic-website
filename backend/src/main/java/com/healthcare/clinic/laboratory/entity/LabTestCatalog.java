package com.healthcare.clinic.laboratory.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "lab_test_catalog")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LabTestCatalog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "test_name", nullable = false)
    private String testName;

    @Column(name = "test_code", nullable = false, unique = true)
    private String testCode;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "reference_range")
    private String referenceRange;

    @Column(length = 50)
    private String unit;

    @Column(length = 100)
    private String category;

    @Column(name = "specimen_type", length = 100)
    private String specimenType;

    @Column(name = "turnaround_target_hours")
    private Integer turnaroundTargetHours;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id")
    private com.healthcare.clinic.branch.entity.Branch branch;

    @Column(length = 100)
    private String department;

    @Column(name = "container_type", length = 100)
    private String containerType;

    @Column(name = "collection_instructions", columnDefinition = "TEXT")
    private String collectionInstructions;

    @Column(length = 100)
    private String method;

    @Column(name = "insurance_eligible")
    @Builder.Default
    private Boolean insuranceEligible = true;

    @Column(name = "preparation_instructions", columnDefinition = "TEXT")
    private String preparationInstructions;
}
