package com.healthcare.clinic.nursing.dto;

import lombok.Data;

@Data
public class NurseEscalationRequest {
    private Long patientId;
    private Long encounterId;
    private Long doctorId;
    private String reason;
    private String clinicalContext;
    private String priority;
}
