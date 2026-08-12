package com.healthcare.clinic.surgery.dto;

import lombok.Data;

@Data
public class SurgeryNoteRequest {
    private Long surgeonId;
    private String preOpDiagnosis;
    private String postOpDiagnosis;
    private String procedurePerformed;
    private String findings;
    private String complications;
}
