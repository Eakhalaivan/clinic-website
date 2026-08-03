package com.healthcare.clinic.nursing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NurseAssignmentResponse {
    private Long id; // assignment ID
    private Long patientId;
    private String patientName;
    private Integer age;
    private String appointmentReason;
    private ZonedDateTime appointmentTime;
    private String attendingDoctorName;
    private String lastVitalsSummary;
    private String status;
    private String insuranceStatus;
    private String injuryStatus;
}
