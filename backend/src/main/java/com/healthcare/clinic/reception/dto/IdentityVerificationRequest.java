package com.healthcare.clinic.reception.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IdentityVerificationRequest {
    private Long patientId;
    private String verificationMethod;
    private String documentReference;
}
