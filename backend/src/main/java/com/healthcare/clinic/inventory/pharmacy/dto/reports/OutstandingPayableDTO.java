package com.healthcare.clinic.inventory.pharmacy.dto.reports;

public record OutstandingPayableDTO(
    String invoiceNumber, String supplier, java.math.BigDecimal totalAmount, String status, int daysOld, String agingBucket
) {}
