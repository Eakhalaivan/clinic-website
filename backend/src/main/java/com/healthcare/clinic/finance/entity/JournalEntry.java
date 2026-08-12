package com.healthcare.clinic.finance.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "journal_entries")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JournalEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "journal_number", unique = true, nullable = false, length = 50)
    private String journalNumber;

    @Column(name = "entry_date", nullable = false)
    private LocalDate entryDate;

    @Column(nullable = false, length = 255)
    private String description;

    @Column(name = "reference_type", length = 50)
    private String referenceType; // e.g. INVOICE, PAYMENT, REFUND

    @Column(name = "reference_id")
    private Long referenceId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private JournalStatus status = JournalStatus.DRAFT;

    @Column(name = "prepared_by")
    private Long preparedBy;

    @Column(name = "approved_by")
    private Long approvedBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @OneToMany(mappedBy = "journalEntry", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<LedgerEntry> lines = new ArrayList<>();

    public enum JournalStatus {
        DRAFT,
        POSTED,
        REVERSED
    }
}
