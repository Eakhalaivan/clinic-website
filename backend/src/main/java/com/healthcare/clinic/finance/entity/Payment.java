package com.healthcare.clinic.finance.entity;

import com.healthcare.clinic.billing.entity.Invoice;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(name = "payment_method", nullable = false, length = 30)
    @Builder.Default
    private String paymentMethod = "CASH";

    @Column(name = "transaction_ref", length = 200)
    private String transactionRef;

    @Column(name = "paid_by")
    private Long paidBy;

    @Column(name = "recorded_by")
    private Long recordedBy;

    @CreationTimestamp
    @Column(name = "paid_at", nullable = false, updatable = false)
    private ZonedDateTime paidAt;

    @Column(columnDefinition = "TEXT")
    private String notes;
}
