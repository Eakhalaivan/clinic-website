package com.healthcare.clinic.superadmin.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Filter;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
@Table(name = "api_keys")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String keyValue; // Encrypted or hashed

    @Column(name = "tenant_id")
    private Long tenantId;

    private String scopes;
    private LocalDateTime expiresAt;
    private boolean revoked;
    private Integer rateLimitLimit;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
