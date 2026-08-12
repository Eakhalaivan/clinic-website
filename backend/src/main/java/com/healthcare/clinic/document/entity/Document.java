package com.healthcare.clinic.document.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.ZonedDateTime;

@Entity
@Table(name = "documents", indexes = {
        @Index(name = "idx_doc_owner", columnList = "owner_type, owner_id"),
        @Index(name = "idx_doc_status", columnList = "status"),
        @Index(name = "idx_doc_branch", columnList = "branch_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Polymorphic owner to tie this document to Patients, Employees, Branches
    @Column(name = "owner_type", nullable = false, length = 50)
    private String ownerType; // PATIENT, EMPLOYEE, BRANCH

    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @Column(name = "document_type", nullable = false, length = 50)
    private String documentType; // ID_PROOF, MEDICAL_RECORD, LAB_REPORT, RADIOLOGY, PRESCRIPTION, INSURANCE, CONSENT, DISCHARGE_SUMMARY, CERTIFICATE

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "storage_key", nullable = false, length = 512)
    private String storageKey; // The object-storage path/key

    @Column(name = "mime_type", length = 100)
    private String mimeType;

    @Column(name = "file_size_bytes")
    private Long fileSizeBytes;

    @Column(name = "original_filename", length = 255)
    private String originalFilename;

    // Versioning
    @Column(name = "version_number", nullable = false)
    @Builder.Default
    private Integer versionNumber = 1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_document_id")
    private Document parentDocument; // Forms a chain of versions

    @Column(nullable = false, length = 50)
    @Builder.Default
    private String status = "ACTIVE"; // ACTIVE, SUPERSEDED, EXPIRED, DELETED

    // OCR
    @Column(name = "ocr_text", columnDefinition = "TEXT")
    private String ocrText;

    @Column(name = "ocr_status", length = 50)
    @Builder.Default
    private String ocrStatus = "NOT_REQUESTED"; // NOT_REQUESTED, PENDING, COMPLETED, FAILED

    // Expiration
    @Column(name = "expires_at")
    private ZonedDateTime expiresAt;

    // Audit
    @Column(name = "uploaded_by_user_id")
    private Long uploadedByUserId;

    @CreatedDate
    @Column(name = "uploaded_at", nullable = false, updatable = false)
    private ZonedDateTime uploadedAt;

    // Scoping
    @Column(name = "branch_id")
    private Long branchId;
}
