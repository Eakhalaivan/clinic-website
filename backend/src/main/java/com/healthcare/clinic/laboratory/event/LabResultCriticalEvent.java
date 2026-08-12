package com.healthcare.clinic.laboratory.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LabResultCriticalEvent {
    private Long patientId;
    private Long labRequestId;
    private String testName;
    private Long doctorId;
    private String resultValue;
}
