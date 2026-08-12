package com.healthcare.clinic.marketing.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.ZonedDateTime;

@Entity
@Table(name = "loyalty_transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoyaltyTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    /**
     * Type: EARNED, REDEEMED, ADJUSTED, EXPIRED, REVERSED, REFUNDED
     */
    @Column(nullable = false, length = 30)
    private String type;

    @Column(nullable = false)
    private Integer points;

    /**
     * Reference type: INVOICE, REDEMPTION, ADMIN, REFERRAL, EXPIRY
     */
    @Column(name = "reference_type", length = 50)
    private String referenceType;

    @Column(name = "reference_id")
    private Long referenceId;

    @Column(name = "balance_before", nullable = false)
    private Integer balanceBefore;

    @Column(name = "balance_after", nullable = false)
    private Integer balanceAfter;

    /**
     * Idempotency key prevents duplicate point awards for the same invoice/event.
     */
    @Column(name = "idempotency_key", length = 100, unique = true)
    private String idempotencyKey;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "approved_by")
    private Long approvedBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;
}
