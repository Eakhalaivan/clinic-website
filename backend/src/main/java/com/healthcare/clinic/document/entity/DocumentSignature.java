package com.healthcare.clinic.document.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.ZonedDateTime;

@Entity
@Table(name = "document_signatures", indexes = {
        @Index(name = "idx_doc_sig_document", columnList = "document_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class DocumentSignature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", nullable = false)
    private Document document;

    @Column(name = "signed_by_user_id", nullable = false)
    private Long signedByUserId;

    @CreatedDate
    @Column(name = "signed_at", nullable = false, updatable = false)
    private ZonedDateTime signedAt;

    @Column(name = "content_hash_at_signing", nullable = false, length = 128)
    private String contentHashAtSigning; // SHA-256 of the file content at sign time

    @Column(name = "ip_address", length = 50)
    private String ipAddress;

    @Column(name = "signature_note", length = 512)
    private String signatureNote;
}
