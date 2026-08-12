package com.healthcare.clinic.nursing.dto;

import lombok.Data;

@Data
public class PainAssessmentRequest {
    private Long patientId;
    private Long encounterId;
    private Integer painScore;
    private String painLocation;
    private String painCharacteristics;
    private String interventions;
}
