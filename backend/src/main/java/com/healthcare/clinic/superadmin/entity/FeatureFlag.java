package com.healthcare.clinic.superadmin.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Filter;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
@Table(name = "feature_flags")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeatureFlag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String flagKey;

    private String description;

    @Column(nullable = false)
    private boolean enabled;

    @Column(name = "tenant_id")
    private Long tenantId; // Null means global

    private String targetRoles; // Comma separated

    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
