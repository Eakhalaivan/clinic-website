package com.healthcare.clinic.nursing.dto;

import lombok.Data;

@Data
public class NursingNoteRequest {
    private Long patientId;
    private Long encounterId;
    private String noteType;
    private String content;
}
