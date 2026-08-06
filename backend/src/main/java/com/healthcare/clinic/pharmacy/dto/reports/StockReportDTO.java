package com.healthcare.clinic.pharmacy.dto.reports;

import com.healthcare.clinic.inventory.entity.BaseEntity;
import com.healthcare.clinic.inventory.entity.Patient;

public record StockReportDTO(
    String medicine, String category, String hsnCode, String batch, Integer quantity, java.math.BigDecimal unitPrice, java.math.BigDecimal mrp, java.time.LocalDate expiry, String supplier, java.math.BigDecimal value
) {}
