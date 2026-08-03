package com.healthcare.clinic.clinicaldecision.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosisAddedEvent {
    private Long patientId;
    private Long recordId;
    private String icd10Code;
    private String diagnosisName;
    private Long doctorId;
}
