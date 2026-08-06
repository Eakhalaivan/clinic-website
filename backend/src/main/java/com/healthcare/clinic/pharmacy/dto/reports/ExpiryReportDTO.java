package com.healthcare.clinic.pharmacy.dto.reports;

import com.healthcare.clinic.inventory.entity.BaseEntity;
import com.healthcare.clinic.inventory.entity.Patient;

public record ExpiryReportDTO(
    String medicine, String batch, java.time.LocalDate expiry, Integer quantity, String supplier, int daysLeft, String urgency
) {}
