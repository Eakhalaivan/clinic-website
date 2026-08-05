package com.healthcare.clinic.appointment.event;

import com.healthcare.clinic.appointment.entity.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentStatusChangedEvent {
    private Long appointmentId;
    private AppointmentStatus oldStatus;
    private AppointmentStatus newStatus;
    private Long doctorId;
    private Long branchId;
}
