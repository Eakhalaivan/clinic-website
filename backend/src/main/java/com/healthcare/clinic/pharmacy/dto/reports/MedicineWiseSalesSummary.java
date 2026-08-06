package com.healthcare.clinic.pharmacy.dto.reports;

import com.healthcare.clinic.inventory.entity.BaseEntity;
import com.healthcare.clinic.inventory.entity.Patient;

import java.math.BigDecimal;

public interface MedicineWiseSalesSummary {
    String getMedicine();
    Integer getUnitsSold();
    BigDecimal getRevenue();
    BigDecimal getTax();
}
