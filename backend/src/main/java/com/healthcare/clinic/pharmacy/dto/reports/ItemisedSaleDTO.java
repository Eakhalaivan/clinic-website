package com.healthcare.clinic.pharmacy.dto.reports;

import com.healthcare.clinic.inventory.entity.BaseEntity;
import com.healthcare.clinic.inventory.entity.Patient;

public record ItemisedSaleDTO(
    String billNumber, java.time.LocalDateTime date, String patient, String doctor, String medicine, String hsnCode, Integer quantity, java.math.BigDecimal unitPrice, java.math.BigDecimal discount, java.math.BigDecimal tax, java.math.BigDecimal netAmount
) {}
