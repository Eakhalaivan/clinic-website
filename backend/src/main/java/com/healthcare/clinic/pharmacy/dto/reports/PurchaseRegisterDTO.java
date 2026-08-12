package com.healthcare.clinic.pharmacy.dto.reports;


public record PurchaseRegisterDTO(
    String grnNumber, java.time.LocalDate date, String supplier, String invoiceNumber, String status, int itemCount
) {}
