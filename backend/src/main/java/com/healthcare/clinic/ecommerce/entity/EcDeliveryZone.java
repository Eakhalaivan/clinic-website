package com.healthcare.clinic.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "ec_delivery_zones")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class EcDeliveryZone {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    private String pincode;

    @Column(length = 100)
    private String city;

    @Column(length = 100)
    private String state;

    @Column(nullable = false, length = 50)
    @Builder.Default
    private String zone = "STANDARD";

    @Column(name = "is_serviceable", nullable = false)
    @Builder.Default
    private Boolean isServiceable = true;

    @Column(name = "min_delivery_days", nullable = false)
    @Builder.Default
    private Integer minDeliveryDays = 1;

    @Column(name = "max_delivery_days", nullable = false)
    @Builder.Default
    private Integer maxDeliveryDays = 5;

    @Column(length = 100)
    private String carrier;

    @Column(name = "free_shipping_above", precision = 10, scale = 2)
    private BigDecimal freeShippingAbove;

    @Column(name = "base_shipping_fee", nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal baseShippingFee = new BigDecimal("49.00");
}
