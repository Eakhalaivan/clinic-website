package com.healthcare.clinic.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "ec_stock_movements")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class EcStockMovement {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "batch_id")
    private Long batchId;

    @Column(name = "branch_id")
    private Long branchId;

    @Column(name = "movement_type", nullable = false, length = 30)
    private String movementType; // RECEIVED, RESERVED, RELEASED, SOLD, RETURNED, DISPOSED, ADJUSTMENT

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "reference_type", length = 50)
    private String referenceType;  // ORDER, RETURN, CART, ADJUSTMENT

    @Column(name = "reference_id")
    private Long referenceId;

    @Column(name = "performed_by")
    private Long performedBy;

    @Column(length = 500)
    private String notes;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private ZonedDateTime createdAt = ZonedDateTime.now();
}
