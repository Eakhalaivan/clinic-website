package com.healthcare.clinic.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "ec_wishlists")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class EcWishlist {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "alert_price_drop", nullable = false)
    @Builder.Default
    private Boolean alertPriceDrop = false;

    @Column(name = "alert_back_in_stock", nullable = false)
    @Builder.Default
    private Boolean alertBackInStock = false;

    @Column(name = "added_at", nullable = false, updatable = false)
    @Builder.Default
    private ZonedDateTime addedAt = ZonedDateTime.now();
}
