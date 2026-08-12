package com.healthcare.clinic.marketing.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "membership_plans")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MembershipPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "validity_days", nullable = false)
    private Integer validityDays;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column
    private Map<String, Object> benefits;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "included_services")
    private List<Long> includedServices;

    @Column(name = "discount_percent", nullable = false, precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal discountPercent = BigDecimal.ZERO;

    @Column(name = "max_dependents", nullable = false)
    @Builder.Default
    private Integer maxDependents = 0;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "branch_ids")
    private List<Long> branchIds;

    @Column(name = "renewal_policy", length = 50)
    private String renewalPolicy;

    @Column(nullable = false, length = 30)
    @Builder.Default
    private String status = "DRAFT"; // DRAFT, ACTIVE, ARCHIVED

    @Column(name = "terms_version", length = 20)
    private String termsVersion;

    @Column(name = "created_by")
    private Long createdBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;
}
