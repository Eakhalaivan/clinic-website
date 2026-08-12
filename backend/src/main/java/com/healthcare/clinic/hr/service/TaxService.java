package com.healthcare.clinic.hr.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TaxService {

    public BigDecimal calculateTax(BigDecimal grossIncome) {
        // Simplified tax bracket logic for demonstration
        if (grossIncome.compareTo(BigDecimal.valueOf(5000)) <= 0) {
            return BigDecimal.ZERO;
        } else if (grossIncome.compareTo(BigDecimal.valueOf(10000)) <= 0) {
            return grossIncome.subtract(BigDecimal.valueOf(5000)).multiply(BigDecimal.valueOf(0.10)); // 10%
        } else {
            BigDecimal baseTax = BigDecimal.valueOf(500); // 10% on the 5k gap
            BigDecimal excessTax = grossIncome.subtract(BigDecimal.valueOf(10000)).multiply(BigDecimal.valueOf(0.20)); // 20%
            return baseTax.add(excessTax);
        }
    }
}
