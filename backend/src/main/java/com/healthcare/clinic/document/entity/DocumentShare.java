package com.healthcare.clinic.document.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "document_shares", indexes = {
        @Index(name = "idx_doc_share_token", columnList = "share_token")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class DocumentShare {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", nullable = false)
    private Document document;

    @Column(name = "shared_with_user_id")
    private Long sharedWithUserId; // For internal sharing

    @Column(name = "share_token", unique = true, length = 100)
    private String shareToken; // For external link-based sharing

    @Column(name = "permission_level", nullable = false, length = 50)
    @Builder.Default
    private String permissionLevel = "VIEW"; // VIEW, DOWNLOAD

    @Column(name = "expires_at")
    private ZonedDateTime expiresAt;

    @Column(name = "created_by_user_id")
    private Long createdByUserId;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @Column(name = "revoked_at")
    private ZonedDateTime revokedAt;

    @PrePersist
    public void generateTokenIfMissing() {
        if (this.sharedWithUserId == null && (this.shareToken == null || this.shareToken.isEmpty())) {
            this.shareToken = UUID.randomUUID().toString();
        }
    }
}
