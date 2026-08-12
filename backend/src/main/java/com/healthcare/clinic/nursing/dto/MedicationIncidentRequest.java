package com.healthcare.clinic.nursing.dto;

import lombok.Data;
import java.time.ZonedDateTime;

@Data
public class MedicationIncidentRequest {
    private Long patientId;
    private String medicationName;
    private String incidentType;
    private ZonedDateTime incidentTime;
    private String description;
    private String actionTaken;
    private Boolean doctorNotified;
}
