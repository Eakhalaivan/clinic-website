package com.healthcare.clinic.finance.service;

import com.healthcare.clinic.finance.dto.PnLResponse;
import com.healthcare.clinic.finance.entity.LedgerEntry;
import com.healthcare.clinic.finance.repository.LedgerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PnLService {

    private final LedgerRepository ledgerRepository;

    @Transactional(readOnly = true)
    public PnLResponse generatePnLStatement(Long branchId, LocalDate startDate, LocalDate endDate) {
        List<LedgerEntry> entries;
        if (branchId != null) {
            entries = ledgerRepository.findByBranchIdAndDateRange(branchId, startDate, endDate);
        } else {
            entries = ledgerRepository.findAllByDateRange(startDate, endDate);
        }

        BigDecimal totalRevenue = BigDecimal.ZERO;
        BigDecimal totalExpenses = BigDecimal.ZERO;

        Map<String, BigDecimal> revenueByCategory = entries.stream()
                .filter(e -> "REVENUE".equalsIgnoreCase(e.getEntryType()))
                .collect(Collectors.groupingBy(LedgerEntry::getCategory,
                        Collectors.reducing(BigDecimal.ZERO, LedgerEntry::getAmount, BigDecimal::add)));

        Map<String, BigDecimal> expenseByCategory = entries.stream()
                .filter(e -> "EXPENSE".equalsIgnoreCase(e.getEntryType()))
                .collect(Collectors.groupingBy(LedgerEntry::getCategory,
                        Collectors.reducing(BigDecimal.ZERO, LedgerEntry::getAmount, BigDecimal::add)));

        for (BigDecimal amount : revenueByCategory.values()) {
            totalRevenue = totalRevenue.add(amount);
        }

        for (BigDecimal amount : expenseByCategory.values()) {
            totalExpenses = totalExpenses.add(amount);
        }

        List<PnLResponse.CategoryBreakdown> revenueBreakdown = revenueByCategory.entrySet().stream()
                .map(e -> PnLResponse.CategoryBreakdown.builder().category(e.getKey()).amount(e.getValue()).build())
                .collect(Collectors.toList());

        List<PnLResponse.CategoryBreakdown> expenseBreakdown = expenseByCategory.entrySet().stream()
                .map(e -> PnLResponse.CategoryBreakdown.builder().category(e.getKey()).amount(e.getValue()).build())
                .collect(Collectors.toList());

        return PnLResponse.builder()
                .totalRevenue(totalRevenue)
                .totalExpenses(totalExpenses)
                .netProfit(totalRevenue.subtract(totalExpenses))
                .period(startDate + " to " + endDate)
                .revenueBreakdown(revenueBreakdown)
                .expenseBreakdown(expenseBreakdown)
                .build();
    }
}
