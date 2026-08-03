package com.healthcare.clinic.doctor.dto;

import lombok.Data;
import java.time.LocalTime;

@Data
public class WorkingHoursRequest {
    private Integer dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer slotDurationMinutes;
    private Boolean isActive;
}
