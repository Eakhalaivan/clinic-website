package com.healthcare.clinic.pharmacy.dto.reports;

import com.healthcare.clinic.inventory.entity.BaseEntity;
import com.healthcare.clinic.inventory.entity.Patient;

public record MedicineWiseSaleDTO(
    String medicine, Integer unitsSold, java.math.BigDecimal revenue, java.math.BigDecimal tax
) {}
