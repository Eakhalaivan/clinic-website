package com.healthcare.clinic.finance.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class PnLResponse {
    private BigDecimal totalRevenue;
    private BigDecimal totalExpenses;
    private BigDecimal netProfit;
    private String period;
    private List<CategoryBreakdown> revenueBreakdown;
    private List<CategoryBreakdown> expenseBreakdown;

    @Data
    @Builder
    public static class CategoryBreakdown {
        private String category;
        private BigDecimal amount;
    }
}
