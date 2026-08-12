package com.healthcare.clinic.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Table(name = "ec_cart_items")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class EcCartItem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private EcCart cart;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "price_snapshot", nullable = false, precision = 10, scale = 2)
    private BigDecimal priceSnapshot;

    @Column(name = "mrp_snapshot", precision = 10, scale = 2)
    private BigDecimal mrpSnapshot;

    @Column(name = "prescription_id")
    private Long prescriptionId;

    @Column(length = 300)
    private String notes;

    @Column(name = "added_at", nullable = false, updatable = false)
    @Builder.Default
    private ZonedDateTime addedAt = ZonedDateTime.now();
}
