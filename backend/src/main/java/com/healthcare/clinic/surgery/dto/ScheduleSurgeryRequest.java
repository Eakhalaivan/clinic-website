package com.healthcare.clinic.surgery.dto;

import lombok.Data;
import java.time.ZonedDateTime;

@Data
public class ScheduleSurgeryRequest {
    private Long patientId;
    private Long surgeonId;
    private Long operationTheatreId;
    private Long admissionId;
    private String surgeryType;
    private String diagnosis;
    private ZonedDateTime scheduledStartTime;
    private Integer estimatedDurationMinutes;
}
