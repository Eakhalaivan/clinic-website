package com.healthcare.clinic.finance.service;

import com.healthcare.clinic.finance.entity.TaxConfiguration;
import com.healthcare.clinic.finance.repository.TaxConfigurationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaxCalculationService {

    private final TaxConfigurationRepository taxRepository;

    public BigDecimal calculateTaxForAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        List<TaxConfiguration> activeTaxes = taxRepository.findActiveTaxes(LocalDate.now());
        
        BigDecimal totalTax = BigDecimal.ZERO;
        for (TaxConfiguration tax : activeTaxes) {
            BigDecimal taxAmount = amount.multiply(tax.getTaxRate())
                    .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
            totalTax = totalTax.add(taxAmount);
        }

        return totalTax;
    }
}
