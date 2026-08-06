package com.healthcare.clinic.pharmacy.dto.reports;

import com.healthcare.clinic.inventory.entity.BaseEntity;
import com.healthcare.clinic.inventory.entity.Patient;

public record SupplierPerformanceDTO(
    String supplier, Double overallScore, Double onTimeDelivery, Double orderFillRate, Double qualityRejection, Double invoiceAccuracy, java.time.LocalDate periodStart, java.time.LocalDate periodEnd
) {}
