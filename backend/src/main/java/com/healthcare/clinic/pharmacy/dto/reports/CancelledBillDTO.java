package com.healthcare.clinic.pharmacy.dto.reports;

import com.healthcare.clinic.inventory.entity.BaseEntity;
import com.healthcare.clinic.inventory.entity.Patient;

public record CancelledBillDTO(
    String billNumber, java.time.LocalDateTime date, String patient, java.math.BigDecimal amount, String cancelledBy
) {}
