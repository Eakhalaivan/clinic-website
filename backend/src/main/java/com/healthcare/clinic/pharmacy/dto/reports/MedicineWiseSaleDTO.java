package com.healthcare.clinic.pharmacy.dto.reports;


public record MedicineWiseSaleDTO(
    String medicine, Integer unitsSold, java.math.BigDecimal revenue, java.math.BigDecimal tax
) {}
