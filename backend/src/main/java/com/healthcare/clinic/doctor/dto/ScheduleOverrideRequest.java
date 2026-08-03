package com.healthcare.clinic.doctor.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ScheduleOverrideRequest {
    private LocalDate overrideDate;
    private Boolean isUnavailable;
    private LocalTime startTime;
    private LocalTime endTime;
    private String reason;
}
