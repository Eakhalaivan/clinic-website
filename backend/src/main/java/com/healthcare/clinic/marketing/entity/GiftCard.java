package com.healthcare.clinic.marketing.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Table(name = "gift_cards")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GiftCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * SHA-256 hash of the plaintext gift card code.
     * The plaintext is only shown once at issuance.
     */
    @Column(name = "code_hash", nullable = false, unique = true, length = 64)
    private String codeHash;

    /**
     * Last 4 characters of the plaintext code for display purposes.
     */
    @Column(name = "code_suffix", nullable = false, length = 4)
    private String codeSuffix;

    @Column(name = "initial_balance", nullable = false, precision = 10, scale = 2)
    private BigDecimal initialBalance;

    @Column(name = "current_balance", nullable = false, precision = 10, scale = 2)
    private BigDecimal currentBalance;

    @Column(name = "issued_to_patient_id")
    private Long issuedToPatientId;

    @Column(name = "purchased_by_patient_id")
    private Long purchasedByPatientId;

    @Column(name = "purchase_invoice_id")
    private Long purchaseInvoiceId;

    @Column(name = "branch_id")
    private Long branchId;

    /**
     * Status: ACTIVE, REDEEMED, EXPIRED, CANCELLED
     */
    @Column(nullable = false, length = 30)
    @Builder.Default
    private String status = "ACTIVE";

    @Column(name = "activated_at", nullable = false)
    @Builder.Default
    private ZonedDateTime activatedAt = ZonedDateTime.now();

    @Column(name = "expires_at")
    private ZonedDateTime expiresAt;

    @Column(name = "cancelled_at")
    private ZonedDateTime cancelledAt;

    @Column(name = "redemption_count", nullable = false)
    @Builder.Default
    private Integer redemptionCount = 0;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;
}
