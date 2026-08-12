package com.healthcare.clinic.appointment.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentBookedEvent {
    private Long appointmentId;
    private Long patientUserId;
    private Long doctorUserId;
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;
    private String doctorName;
    private String patientEmail; // We'd ideally fetch this from identity-service or pass it along, but for now we'll simplify.
}
