package com.healthcare.clinic.radiology.entity;

import com.healthcare.clinic.identity.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;

@Entity
@Table(name = "radiology_annotations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RadiologyAnnotation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id", nullable = false)
    private DicomStudy study;

    @Column(name = "series_instance_uid", length = 255)
    private String seriesInstanceUid;

    @Column(name = "sop_instance_uid", length = 255)
    private String sopInstanceUid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @Column(name = "annotation_type", nullable = false, length = 50)
    private String annotationType; // e.g. "LENGTH", "ANGLE", "TEXT", "ARROW"

    @org.hibernate.annotations.JdbcTypeCode(org.hibernate.type.SqlTypes.JSON)
    @Column(name = "annotation_data", nullable = false)
    private java.util.Map<String, Object> annotationData;

    @Column(nullable = false, length = 20)
    @Builder.Default
    private String status = "DRAFT"; // DRAFT, FINALIZED

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;
}
