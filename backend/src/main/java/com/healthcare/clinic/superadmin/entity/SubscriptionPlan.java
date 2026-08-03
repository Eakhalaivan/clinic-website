package com.healthcare.clinic.superadmin.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Table(name = "subscription_plans")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "plan_name", nullable = false, unique = true, length = 100)
    private String planName;

    @Column(name = "price_monthly", nullable = false, precision = 12, scale = 2)
    private BigDecimal priceMonthly;

    @Column(name = "price_annually", precision = 12, scale = 2)
    private BigDecimal priceAnnually;

    @Column(name = "max_users", nullable = false)
    @Builder.Default
    private Integer maxUsers = 10;

    @Column(name = "max_branches", nullable = false)
    @Builder.Default
    private Integer maxBranches = 1;

    @Column(columnDefinition = "TEXT")
    private String features; // JSON array string

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;
}
