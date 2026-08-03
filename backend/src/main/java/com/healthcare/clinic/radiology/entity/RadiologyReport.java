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
    private String status = "DRAFT"; // DRAFT, FINALIZED

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @Column(name = "finalized_at")
    private ZonedDateTime finalizedAt;
}
