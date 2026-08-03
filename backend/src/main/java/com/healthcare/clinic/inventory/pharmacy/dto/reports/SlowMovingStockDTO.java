package com.healthcare.clinic.inventory.pharmacy.dto.reports;

public record SlowMovingStockDTO(
    String medicine, int soldInPeriod
) {}
