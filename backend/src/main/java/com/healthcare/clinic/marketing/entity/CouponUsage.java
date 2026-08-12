package com.healthcare.clinic.marketing.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Table(name = "coupon_usages")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "coupon_id", nullable = false)
    private Long couponId;

    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @Column(name = "invoice_id")
    private Long invoiceId;

    @Column(name = "applied_at", nullable = false)
    @Builder.Default
    private ZonedDateTime appliedAt = ZonedDateTime.now();

    @Column(name = "discount_applied", nullable = false, precision = 10, scale = 2)
    private BigDecimal discountApplied;

    @Column(name = "branch_id")
    private Long branchId;
}
