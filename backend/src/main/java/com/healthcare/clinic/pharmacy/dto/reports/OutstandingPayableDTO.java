package com.healthcare.clinic.pharmacy.dto.reports;


public record OutstandingPayableDTO(
    String invoiceNumber, String supplier, java.math.BigDecimal totalAmount, String status, int daysOld, String agingBucket
) {}
