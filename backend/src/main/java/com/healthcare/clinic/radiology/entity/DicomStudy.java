package com.healthcare.clinic.radiology.entity;

import com.healthcare.clinic.patient.entity.PatientProfile;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.ZonedDateTime;

@Entity
@Table(name = "dicom_studies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DicomStudy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "study_instance_uid", nullable = false, unique = true, length = 255)
    private String studyInstanceUid;

    @Column(name = "accession_number", length = 100)
    private String accessionNumber;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id", nullable = false)
    private ImagingRequest request;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private PatientProfile patient;

    @Column(nullable = false, length = 30)
    private String modality;

    @Column(name = "study_date")
    private ZonedDateTime studyDate;

    @Column(name = "series_count", nullable = false)
    @Builder.Default
    private Integer seriesCount = 0;

    @Column(name = "instance_count", nullable = false)
    @Builder.Default
    private Integer instanceCount = 0;

    @Column(name = "storage_path", length = 500)
    private String storagePath;

    @Column(nullable = false, length = 30)
    @Builder.Default
    private String status = "PENDING"; // PENDING, ACQUIRED, PROCESSING, AVAILABLE_FOR_REPORTING, REPORTED, VERIFIED, ARCHIVED, DELETED

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "technician_id")
    private com.healthcare.clinic.identity.entity.User technician;

    @Column(name = "acquisition_device", length = 100)
    private String acquisitionDevice;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;
}
