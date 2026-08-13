package com.healthcare.clinic.inpatient.dto;

import com.healthcare.clinic.inpatient.entity.DischargeSummary;
import lombok.Data;

@Data
public class DischargeRequest {
    private Long dischargingDoctorId;
    private DischargeSummary summaryData;
}
