package com.healthcare.clinic.tenant.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.ZonedDateTime;

@Entity(name = "TenantFeatureFlag")
@Table(name = "feature_flags")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class FeatureFlag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "flag_key", nullable = false, length = 100, unique = true)
    private String flagKey;

    @Column(name = "is_enabled")
    @Builder.Default
    private Boolean isEnabled = false;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "allowlist_tenants_json", columnDefinition = "TEXT")
    private String allowlistTenantsJson; // JSON array of tenant IDs where this flag is forcibly enabled

    @CreatedDate
    @Column(updatable = false)
    private ZonedDateTime createdAt;

    @LastModifiedDate
    private ZonedDateTime updatedAt;
}
