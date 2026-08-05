package com.healthcare.clinic.patient.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimelineEventDTO {
    private String id;
    private String type; // "RADIOLOGY", "PRESCRIPTION", "CLINICAL_NOTE", "INVOICE"
    private String title;
    private String description;
    private String status;
    private ZonedDateTime eventDate;
    private Long referenceId;
}
