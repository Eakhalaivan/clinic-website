package com.healthcare.clinic.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "ec_stock_reservations")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class EcStockReservation {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cart_id", nullable = false)
    private Long cartId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "batch_id")
    private Long batchId;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "expires_at", nullable = false)
    private ZonedDateTime expiresAt;

    @Column(nullable = false, length = 20)
    @Builder.Default
    private String status = "ACTIVE"; // ACTIVE, RELEASED, CONVERTED

    @Column(name = "released_at")
    private ZonedDateTime releasedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private ZonedDateTime createdAt = ZonedDateTime.now();
}
