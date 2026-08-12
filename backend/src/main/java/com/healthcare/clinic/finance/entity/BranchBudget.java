package com.healthcare.clinic.finance.entity;

import com.healthcare.clinic.branch.entity.Branch;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;

@Entity
@Table(name = "branch_budgets", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"branch_id", "budget_year", "budget_month"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BranchBudget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @Column(name = "budget_year", nullable = false)
    private Integer budgetYear;

    @Column(name = "budget_month", nullable = false)
    private Integer budgetMonth;

    @Column(name = "allocated_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal allocatedAmount;

    @Column(name = "spent_amount", nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal spentAmount = BigDecimal.ZERO;

    @Column(nullable = false, length = 30)
    @Builder.Default
    private String status = "ACTIVE"; // ACTIVE, EXCEEDED, CLOSED

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    public BigDecimal getRemainingAmount() {
        if (allocatedAmount == null) return BigDecimal.ZERO;
        return allocatedAmount.subtract(spentAmount != null ? spentAmount : BigDecimal.ZERO);
    }
}
