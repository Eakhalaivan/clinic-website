package com.healthcare.clinic.inpatient.dto;

import lombok.Data;

@Data
public class AdmissionRequest {
    private Long patientId;
    private Long doctorId;
    private Long bedId;
    private String admissionType;
    private String reason;
}
