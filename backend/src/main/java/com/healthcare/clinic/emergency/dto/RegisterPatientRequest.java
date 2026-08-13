package com.healthcare.clinic.emergency.dto;

import lombok.Data;

@Data
public class RegisterPatientRequest {
    private Long patientId;
    private String arrivalMode;
}
