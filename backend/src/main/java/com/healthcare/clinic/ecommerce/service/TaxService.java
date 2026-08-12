package com.healthcare.clinic.ecommerce.service;

import com.healthcare.clinic.ecommerce.entity.EcTaxRule;
import com.healthcare.clinic.ecommerce.repository.EcTaxRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Optional;

@Service("ecommerceTaxService")
@RequiredArgsConstructor
public class TaxService {

    private final EcTaxRuleRepository taxRuleRepository;

    @Transactional(readOnly = true)
    public EcTaxRule getApplicableTaxRule(String taxClass, String state) {
        LocalDate today = LocalDate.now();
        
        Optional<EcTaxRule> stateSpecificRule = taxRuleRepository.findAll().stream()
                .filter(r -> Boolean.TRUE.equals(r.getIsActive())
                        && r.getTaxClass().equals(taxClass)
                        && r.getState().equalsIgnoreCase(state)
                        && !r.getEffectiveFrom().isAfter(today)
                        && (r.getEffectiveTo() == null || !r.getEffectiveTo().isBefore(today)))
                .findFirst();

        if (stateSpecificRule.isPresent()) return stateSpecificRule.get();

        return taxRuleRepository.findAll().stream()
                .filter(r -> Boolean.TRUE.equals(r.getIsActive())
                        && r.getTaxClass().equals(taxClass)
                        && r.getState().equals("ALL")
                        && !r.getEffectiveFrom().isAfter(today)
                        && (r.getEffectiveTo() == null || !r.getEffectiveTo().isBefore(today)))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No active tax rule found for tax class: " + taxClass));
    }

    public BigDecimal calculateTax(BigDecimal amount, EcTaxRule rule) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) == 0 || rule.getRatePercent().compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return amount.multiply(rule.getRatePercent())
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
    }
}
