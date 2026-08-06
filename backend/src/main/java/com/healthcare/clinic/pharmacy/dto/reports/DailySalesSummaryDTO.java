package com.healthcare.clinic.pharmacy.dto.reports;

import com.healthcare.clinic.inventory.entity.BaseEntity;
import com.healthcare.clinic.inventory.entity.Patient;

public record DailySalesSummaryDTO(
    java.math.BigDecimal totalRevenue, java.math.BigDecimal totalTax, java.math.BigDecimal totalDiscount, java.math.BigDecimal netRevenue, long billCount, long cashBills, long creditBills, String period
) {}
