package com.healthcare.clinic.notification.event;

import lombok.*;
import java.time.ZonedDateTime;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class AppointmentCancelledEvent {
    private Long appointmentId;
    private Long patientId;
    private Long doctorId;
    private ZonedDateTime startTime;
    private String doctorName;
    private String patientEmail;
    private Long branchId;
}
