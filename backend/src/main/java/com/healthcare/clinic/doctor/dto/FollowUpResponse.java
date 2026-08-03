package com.healthcare.clinic.doctor.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FollowUpResponse {
    private Long id;
    private String patientName;
    private String phone;
    private String followUpDate;
    private String reason;
    private String status;
}
