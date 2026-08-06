package com.healthcare.clinic.pharmacy.dto.reports;

import com.healthcare.clinic.inventory.entity.BaseEntity;
import com.healthcare.clinic.inventory.entity.Patient;

public record PurchaseRegisterDTO(
    String grnNumber, java.time.LocalDate date, String supplier, String invoiceNumber, String status, int itemCount
) {}
