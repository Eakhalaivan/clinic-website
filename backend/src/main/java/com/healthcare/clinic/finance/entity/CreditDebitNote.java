package com.healthcare.clinic.finance.entity;

import com.healthcare.clinic.billing.entity.Invoice;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Table(name = "credit_debit_notes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreditDebitNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "note_number", unique = true, length = 50, nullable = false)
    private String noteNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "note_type", nullable = false, length = 10)
    private NoteType type; // CREDIT or DEBIT

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, length = 255)
    private String reason;

    @Column(name = "issued_by")
    private Long issuedBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    public enum NoteType {
        CREDIT,
        DEBIT
    }
}
