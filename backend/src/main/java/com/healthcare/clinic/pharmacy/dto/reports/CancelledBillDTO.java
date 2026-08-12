package com.healthcare.clinic.pharmacy.dto.reports;


public record CancelledBillDTO(
    String billNumber, java.time.LocalDateTime date, String patient, java.math.BigDecimal amount, String cancelledBy
) {}
