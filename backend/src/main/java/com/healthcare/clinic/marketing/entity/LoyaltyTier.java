package com.healthcare.clinic.marketing.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Map;

@Entity
@Table(name = "loyalty_tiers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoyaltyTier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @Column(name = "min_points", nullable = false)
    private Integer minPoints;

    @Column(name = "max_points")
    private Integer maxPoints;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column
    private Map<String, Object> benefits;

    @Column(name = "earning_multiplier", nullable = false, precision = 4, scale = 2)
    @Builder.Default
    private BigDecimal earningMultiplier = BigDecimal.ONE;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "branch_scope")
    private Map<String, Object> branchScope;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;
}
