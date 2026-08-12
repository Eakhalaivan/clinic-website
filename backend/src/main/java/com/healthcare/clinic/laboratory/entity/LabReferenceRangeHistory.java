package com.healthcare.clinic.laboratory.entity;

import com.healthcare.clinic.identity.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;

@Entity
@Table(name = "lab_reference_range_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LabReferenceRangeHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_catalog_id", nullable = false)
    private LabTestCatalog testCatalog;

    @Column(name = "reference_range", nullable = false)
    private String referenceRange;

    @Column(name = "valid_from", updatable = false)
    @Builder.Default
    private ZonedDateTime validFrom = ZonedDateTime.now();

    @Column(name = "valid_to")
    private ZonedDateTime validTo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private User updatedBy;
}
