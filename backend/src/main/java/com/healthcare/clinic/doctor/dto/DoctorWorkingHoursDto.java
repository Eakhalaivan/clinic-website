package com.healthcare.clinic.doctor.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalTime;

@Data
@Builder
public class DoctorWorkingHoursDto {
    private Long id;
    private Long doctorId;
    private Integer dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer slotDurationMinutes;
    private Boolean isActive;
}
