package com.healthcare.clinic.nursing.dto;

import lombok.Data;

@Data
public class NursingCarePlanRequest {
    private Long patientId;
    private Long encounterId;
    private String diagnosis;
    private String goals;
    private String interventions;
    private String status;
}
