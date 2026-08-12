package com.healthcare.clinic.integration.entity;

import com.healthcare.clinic.tenant.entity.Tenant;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.ZonedDateTime;

@Entity
@Table(name = "integration_configs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class IntegrationConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Tenant tenant; // Nullable if global config

    @Column(name = "provider_name", nullable = false, length = 100)
    private String providerName; // e.g., STRIPE, TWILIO, AWS_S3

    @Column(name = "integration_type", nullable = false, length = 100)
    private String integrationType; // e.g., PAYMENT, SMS, STORAGE

    @Column(name = "config_json", columnDefinition = "TEXT")
    private String configJson; // Store non-sensitive configuration

    @Column(name = "secrets_vault_path", length = 255)
    private String secretsVaultPath; // Path in external vault for secrets

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = false;

    @Column(name = "health_status", length = 50)
    @Builder.Default
    private String healthStatus = "UNKNOWN";

    @CreatedDate
    @Column(updatable = false)
    private ZonedDateTime createdAt;

    @LastModifiedDate
    private ZonedDateTime updatedAt;
}
