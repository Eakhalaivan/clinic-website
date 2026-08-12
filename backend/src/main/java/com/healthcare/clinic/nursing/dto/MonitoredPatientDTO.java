package com.healthcare.clinic.nursing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonitoredPatientDTO {
    private Long assignmentId;
    private Long patientId;
    private Long encounterId;
    private Long bedId;
    private String bedNumber;
    private String status; // ACTIVE
}
