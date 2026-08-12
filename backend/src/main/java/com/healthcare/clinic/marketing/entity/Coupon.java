package com.healthcare.clinic.marketing.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(name = "coupons")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(name = "discount_type", nullable = false, length = 20)
    @Builder.Default
    private String discountType = "PERCENTAGE"; // PERCENTAGE, FIXED_AMOUNT

    @Column(name = "discount_value", nullable = false, precision = 10, scale = 2)
    private BigDecimal discountValue;

    @Column(name = "min_order_amount", precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal minOrderAmount = BigDecimal.ZERO;

    @Column(name = "max_discount", precision = 10, scale = 2)
    private BigDecimal maxDiscount;

    @Column(name = "valid_from", nullable = false)
    private LocalDate validFrom;

    @Column(name = "valid_to", nullable = false)
    private LocalDate validTo;

    @Column(name = "usage_limit")
    @Builder.Default
    private Integer usageLimit = 100;

    @Column(name = "times_used", nullable = false)
    @Builder.Default
    private Integer timesUsed = 0;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    // Extended fields for Phase 16
    @Column(name = "campaign_id")
    private Long campaignId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "eligible_service_ids")
    private List<Long> eligibleServiceIds;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "branch_ids")
    private List<Long> branchIds;

    @Column(name = "segment_id")
    private Long segmentId;

    @Column(name = "per_patient_limit", nullable = false)
    @Builder.Default
    private Integer perPatientLimit = 1;

    @Column(name = "is_stackable", nullable = false)
    @Builder.Default
    private Boolean isStackable = false;

    @Column(name = "approved_by")
    private Long approvedBy;

    @Column(name = "approved_at")
    private ZonedDateTime approvedAt;

    @Column(length = 100)
    private String purpose;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_at")
    @Builder.Default
    private ZonedDateTime createdAt = ZonedDateTime.now();

    @UpdateTimestamp
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;
}
