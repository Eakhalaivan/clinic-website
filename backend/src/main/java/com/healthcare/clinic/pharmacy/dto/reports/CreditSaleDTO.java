package com.healthcare.clinic.pharmacy.dto.reports;

import com.healthcare.clinic.inventory.entity.BaseEntity;
import com.healthcare.clinic.inventory.entity.Patient;

public record CreditSaleDTO(
    String billNumber, java.time.LocalDateTime date, String patient, java.math.BigDecimal netAmount, java.math.BigDecimal paidAmount, java.math.BigDecimal balanceAmount, String status
) {}
