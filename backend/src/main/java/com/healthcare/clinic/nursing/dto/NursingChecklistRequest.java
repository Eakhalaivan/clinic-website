package com.healthcare.clinic.nursing.dto;

import lombok.Data;

@Data
public class NursingChecklistRequest {
    private Long patientId;
    private Long encounterId;
    private String checklistType;
    private String itemsJson;
}
