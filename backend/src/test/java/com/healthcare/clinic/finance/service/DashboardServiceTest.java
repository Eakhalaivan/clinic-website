package com.healthcare.clinic.finance.service;

import com.healthcare.clinic.branch.entity.Branch;
import com.healthcare.clinic.finance.dto.DashboardResponse;
import com.healthcare.clinic.finance.dto.PnLResponse;
import com.healthcare.clinic.finance.entity.BranchBudget;
import com.healthcare.clinic.finance.entity.Expense;
import com.healthcare.clinic.finance.entity.ExpenseStatus;
import com.healthcare.clinic.finance.entity.InsuranceClaim;
import com.healthcare.clinic.finance.entity.ClaimStatus;
import com.healthcare.clinic.finance.repository.BranchBudgetRepository;
import com.healthcare.clinic.finance.repository.ExpenseRepository;
import com.healthcare.clinic.finance.repository.InsuranceClaimRepository;
import com.healthcare.clinic.finance.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    @Mock private PaymentRepository paymentRepository;
    @Mock private ExpenseRepository expenseRepository;
    @Mock private InsuranceClaimRepository claimRepository;
    @Mock private BranchBudgetRepository budgetRepository;
    @Mock private PnLService pnLService;

    @InjectMocks
    private DashboardService dashboardService;

    @Test
    void getRealtimeDashboardData() {
        // Arrange
        PnLResponse pnlResponse = PnLResponse.builder()
                .totalRevenue(new BigDecimal("1000"))
                .totalExpenses(new BigDecimal("200"))
                .netProfit(new BigDecimal("800"))
                .build();
        when(pnLService.generatePnLStatement(any(), any(), any())).thenReturn(pnlResponse);

        Expense pendingExpense = new Expense();
        pendingExpense.setStatus(ExpenseStatus.PENDING_APPROVAL);
        when(expenseRepository.findAll()).thenReturn(List.of(pendingExpense));

        InsuranceClaim claim = new InsuranceClaim();
        claim.setStatus(ClaimStatus.APPROVED);
        when(claimRepository.findAll()).thenReturn(List.of(claim));

        Branch branch = new Branch();
        branch.setName("Main");
        BranchBudget budget = new BranchBudget();
        budget.setBranch(branch);
        budget.setBudgetYear(2023);
        budget.setBudgetMonth(1);
        budget.setAllocatedAmount(new BigDecimal("5000"));
        budget.setSpentAmount(new BigDecimal("1000"));
        when(budgetRepository.findAll()).thenReturn(List.of(budget));

        // Act
        DashboardResponse response = dashboardService.getRealtimeDashboardData(1L, LocalDate.now().minusDays(30), LocalDate.now());

        // Assert
        assertThat(response.getTotalRevenue()).isEqualTo(new BigDecimal("1000"));
        assertThat(response.getTotalExpenses()).isEqualTo(new BigDecimal("200"));
        assertThat(response.getNetProfit()).isEqualTo(new BigDecimal("800"));
        assertThat(response.getPendingExpenseApprovals()).isEqualTo(1L);
        assertThat(response.getClaimsByStatus()).containsEntry("APPROVED", 1L);
        assertThat(response.getBranchBudgetStatus()).containsEntry("Main (2023-1)", new BigDecimal("4000"));
    }
}
