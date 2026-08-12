package com.healthcare.clinic.reception.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IdentityVerificationResponse {
    private Long id;
    private Long patientId;
    private String verificationMethod;
    private Long verifiedByUserId;
    private ZonedDateTime verifiedAt;
    private String status;
    private String failureReason;
    private String documentReference;
}
