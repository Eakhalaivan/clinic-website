package com.healthcare.clinic.superadmin.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Filter;
import lombok.*;

@Entity(name="SuperadminIntegrationConfig")
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
@Table(name = "integration_configs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IntegrationConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="provider_name", nullable = false)
    private String providerName;

    @Column(name="integration_type", nullable = false)
    private String integrationType; // SMS, PAYMENT, LAB

    @Column(name = "tenant_id")
    private Long tenantId;

    @Column(columnDefinition = "TEXT")
    private String encryptedCredentials;

    private boolean active;
}
