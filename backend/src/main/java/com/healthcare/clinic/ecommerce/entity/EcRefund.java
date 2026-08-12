package com.healthcare.clinic.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Table(name = "ec_refunds")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class EcRefund {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "return_id")
    private Long returnId;

    @Column(name = "payment_id")
    private Long paymentId;

    @Column(name = "idempotency_key", nullable = false, unique = true, length = 128)
    private String idempotencyKey;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, length = 30)
    @Builder.Default
    private String method = "ORIGINAL"; // ORIGINAL, STORE_CREDIT, CREDIT_NOTE

    @Column(nullable = false, length = 30)
    @Builder.Default
    private String status = "PENDING"; // PENDING, PROCESSING, SUCCESSFUL, FAILED

    @Column(name = "approved_by")
    private Long approvedBy;

    @Column(name = "provider_ref", length = 200)
    private String providerRef;

    @Column(name = "failure_reason", length = 500)
    private String failureReason;

    @Column(name = "processed_at")
    private ZonedDateTime processedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private ZonedDateTime createdAt = ZonedDateTime.now();

    @UpdateTimestamp
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;
}
