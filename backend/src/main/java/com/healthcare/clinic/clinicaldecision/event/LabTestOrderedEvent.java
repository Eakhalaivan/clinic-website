package com.healthcare.clinic.clinicaldecision.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LabTestOrderedEvent {
    private Long patientId;
    private Long labRequestId;
    private String testName;
    private Long doctorId;
}
