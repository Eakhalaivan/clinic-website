package com.healthcare.clinic.laboratory.entity;

import com.healthcare.clinic.branch.entity.Branch;
import com.healthcare.clinic.identity.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.ZonedDateTime;

@Entity
@Table(name = "lab_quality_controls")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LabQualityControl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_catalog_id", nullable = false)
    private LabTestCatalog testCatalog;

    @Column(nullable = false, length = 50)
    private String status; // PASSED, FAILED, WARNING

    @Column(columnDefinition = "TEXT")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performed_by", nullable = false)
    private User performedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @CreationTimestamp
    @Column(name = "performed_at", updatable = false)
    private ZonedDateTime performedAt;
}
