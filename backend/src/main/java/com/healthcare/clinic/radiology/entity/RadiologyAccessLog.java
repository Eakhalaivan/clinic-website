package com.healthcare.clinic.radiology.entity;

import com.healthcare.clinic.identity.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.ZonedDateTime;

@Entity
@Table(name = "radiology_access_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RadiologyAccessLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id")
    private ImagingRequest request;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dicom_study_id")
    private DicomStudy dicomStudy;

    @Column(name = "access_type", nullable = false, length = 50)
    private String accessType; // VIEW_REPORT, DOWNLOAD_REPORT, VIEW_DICOM_STUDY

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @CreationTimestamp
    @Column(name = "accessed_at", nullable = false, updatable = false)
    private ZonedDateTime accessedAt;
}
