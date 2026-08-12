package com.healthcare.clinic.laboratory.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "lab_sample_collections")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LabSampleCollection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id", nullable = false)
    private LabTestRequest request;

    @Column(name = "sample_type", length = 100)
    private String sampleType;

    @Column(name = "collector_name", length = 100)
    private String collectorName;

    @Column(columnDefinition = "TEXT")
    private String remarks;

    @Column(name = "sample_image_url", length = 255)
    private String sampleImageUrl;

    @Column(name = "collected_at")
    @Builder.Default
    private ZonedDateTime collectedAt = ZonedDateTime.now();

    @Column(name = "storage_state", length = 50)
    private String storageState;

    @org.hibernate.annotations.JdbcTypeCode(org.hibernate.type.SqlTypes.JSON)
    @Column(name = "chain_of_custody")
    private java.util.Map<String, Object> chainOfCustody;

    @Column(name = "rejection_reason", length = 100)
    private String rejectionReason;
}
