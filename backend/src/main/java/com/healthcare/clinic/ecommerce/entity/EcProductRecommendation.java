package com.healthcare.clinic.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Table(name = "ec_product_recommendations")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class EcProductRecommendation {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "related_product_id", nullable = false)
    private Long relatedProductId;

    @Column(name = "relation_type", nullable = false, length = 30)
    @Builder.Default
    private String relationType = "RELATED"; // RELATED, FBT, REPLENISHMENT

    @Column(nullable = false, precision = 5, scale = 4)
    @Builder.Default
    private BigDecimal score = new BigDecimal("1.0000");

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private ZonedDateTime createdAt = ZonedDateTime.now();
}
