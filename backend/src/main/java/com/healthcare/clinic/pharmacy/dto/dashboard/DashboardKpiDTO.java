package com.healthcare.clinic.pharmacy.dto.dashboard;

import com.healthcare.clinic.inventory.entity.BaseEntity;
import com.healthcare.clinic.inventory.entity.Patient;

import java.util.HashMap;

public class DashboardKpiDTO extends HashMap<String, Object> {
    // Extends HashMap to allow dynamic key-value pairs based on role config
    // (e.g. totalSkus, todayRevenue, pendingPrescriptions)
}
