package com.healthcare.clinic.pharmacy.dto.reports;

import com.healthcare.clinic.inventory.entity.BaseEntity;
import com.healthcare.clinic.inventory.entity.Patient;

public record OutstandingPayableDTO(
    String invoiceNumber, String supplier, java.math.BigDecimal totalAmount, String status, int daysOld, String agingBucket
) {}
