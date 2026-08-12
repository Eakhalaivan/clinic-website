package com.healthcare.clinic.finance.entity;

import com.healthcare.clinic.branch.entity.Branch;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;

@Entity
@Table(name = "daily_closings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyClosing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @Column(name = "closing_date", nullable = false)
    private LocalDate closingDate;

    @Column(name = "total_revenue", nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal totalRevenue = BigDecimal.ZERO;

    @Column(name = "total_collections", nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal totalCollections = BigDecimal.ZERO;
    
    @Column(name = "total_refunds", nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal totalRefunds = BigDecimal.ZERO;

    @Column(name = "net_deposit", nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal netDeposit = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private ClosingStatus status = ClosingStatus.DRAFT;

    @Column(name = "closed_by")
    private Long closedBy;

    @Column(name = "approved_by")
    private Long approvedBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    public enum ClosingStatus {
        DRAFT,
        SUBMITTED,
        APPROVED,
        REJECTED
    }
}
