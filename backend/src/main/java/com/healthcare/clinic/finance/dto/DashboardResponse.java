package com.healthcare.clinic.finance.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
public class DashboardResponse {
    private BigDecimal totalRevenue;
    private BigDecimal totalExpenses;
    private BigDecimal netProfit;
    
    private Map<String, Long> claimsByStatus;
    private Long pendingExpenseApprovals;
    
    private Map<String, BigDecimal> branchBudgetStatus; // Branch name -> remaining budget
}
