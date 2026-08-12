package com.healthcare.clinic.radiology.entity;

import com.healthcare.clinic.identity.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.ZonedDateTime;

@Entity
@Table(name = "radiology_reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RadiologyReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id", nullable = false, unique = true)
    private ImagingRequest request;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "radiologist_id")
    private User radiologist;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String findings;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String impression;

    @Column(name = "dicom_study_uid")
    private String dicomStudyUid;

    @Column(name = "dicom_image_url", length = 500)
    private String dicomImageUrl;

    @Column(nullable = false, length = 20)
    @Builder.Default
    private String status = "DRAFT"; // DRAFT, FINALIZED, VERIFIED

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @Column(name = "finalized_at")
    private ZonedDateTime finalizedAt;

    @Column(name = "verified_at")
    private ZonedDateTime verifiedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "verified_by")
    private User verifiedBy;

    @Column(name = "is_addendum", nullable = false)
    @Builder.Default
    private Boolean isAddendum = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_report_id")
    private RadiologyReport parentReport;

    @org.hibernate.annotations.JdbcTypeCode(org.hibernate.type.SqlTypes.JSON)
    @Column(name = "structured_data")
    private java.util.Map<String, Object> structuredData;
}
