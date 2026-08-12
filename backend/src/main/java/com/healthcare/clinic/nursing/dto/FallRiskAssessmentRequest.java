package com.healthcare.clinic.nursing.dto;

import lombok.Data;

@Data
public class FallRiskAssessmentRequest {
    private Long patientId;
    private Long encounterId;
    private Integer score;
    private String notes;
}
