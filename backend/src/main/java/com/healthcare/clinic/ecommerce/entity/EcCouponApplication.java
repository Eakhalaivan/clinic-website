package com.healthcare.clinic.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Table(name = "ec_coupon_applications")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class EcCouponApplication {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "coupon_id", nullable = false)
    private Long couponId;

    @Column(name = "coupon_code", nullable = false, length = 100)
    private String couponCode;

    @Column(name = "discount_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal discountAmount;

    @Column(name = "applied_at", nullable = false, updatable = false)
    @Builder.Default
    private ZonedDateTime appliedAt = ZonedDateTime.now();

    @Column(name = "reversed_at")
    private ZonedDateTime reversedAt;
}
