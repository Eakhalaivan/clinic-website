package com.healthcare.clinic.nursing.dto;

import lombok.Data;
import java.time.ZonedDateTime;

@Data
public class NursingTaskRequest {
    private Long patientId;
    private Long encounterId;
    private Long assignedTo;
    private String taskType;
    private String description;
    private ZonedDateTime dueTime;
}
