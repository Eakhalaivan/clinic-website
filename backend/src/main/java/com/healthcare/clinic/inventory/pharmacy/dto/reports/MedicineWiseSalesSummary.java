package com.healthcare.clinic.inventory.pharmacy.dto.reports;

import java.math.BigDecimal;

public interface MedicineWiseSalesSummary {
    String getMedicine();
    Integer getUnitsSold();
    BigDecimal getRevenue();
    BigDecimal getTax();
}
