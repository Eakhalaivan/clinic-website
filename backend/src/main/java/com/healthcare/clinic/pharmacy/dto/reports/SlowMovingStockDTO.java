package com.healthcare.clinic.pharmacy.dto.reports;

import com.healthcare.clinic.inventory.entity.BaseEntity;
import com.healthcare.clinic.inventory.entity.Patient;

public record SlowMovingStockDTO(
    String medicine, int soldInPeriod
) {}
