package com.healthcare.clinic.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Table(name = "ec_stock_batches")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class EcStockBatch {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "branch_id")
    private Long branchId;

    @Column(name = "batch_number", nullable = false, length = 100)
    private String batchNumber;

    @Column(name = "expiry_date")
    private java.time.LocalDate expiryDate;

    @Column(name = "manufactured_date")
    private java.time.LocalDate manufacturedDate;

    @Column(name = "quantity_total", nullable = false)
    @Builder.Default
    private Integer quantityTotal = 0;

    @Column(name = "quantity_available", nullable = false)
    @Builder.Default
    private Integer quantityAvailable = 0;

    @Column(name = "quantity_reserved", nullable = false)
    @Builder.Default
    private Integer quantityReserved = 0;

    @Column(name = "is_quarantined", nullable = false)
    @Builder.Default
    private Boolean isQuarantined = false;

    @Column(name = "is_recalled", nullable = false)
    @Builder.Default
    private Boolean isRecalled = false;

    @Column(name = "quarantine_reason", length = 500)
    private String quarantineReason;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private ZonedDateTime createdAt = ZonedDateTime.now();

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;
}
