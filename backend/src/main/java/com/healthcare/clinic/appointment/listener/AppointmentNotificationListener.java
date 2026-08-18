package com.healthcare.clinic.appointment.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthcare.clinic.appointment.event.AppointmentBookedEvent;
import com.healthcare.clinic.doctor.entity.ClinicOutboxEvent;
import com.healthcare.clinic.doctor.repository.ClinicOutboxEventRepository;
import com.healthcare.clinic.notification.event.AppointmentCancelledEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class AppointmentNotificationListener {

    private final ClinicOutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

    @EventListener
    public void onAppointmentBooked(AppointmentBookedEvent event) {
        try {
            String payload = objectMapper.writeValueAsString(Map.of(
                    "appointmentId", event.getAppointmentId(),
                    "patientUserId", event.getPatientUserId(),
                    "doctorUserId", event.getDoctorUserId(),
                    "startTime", event.getStartTime().toString(),
                    "doctorName", event.getDoctorName()
            ));

            ClinicOutboxEvent outboxEvent = ClinicOutboxEvent.builder()
                    .aggregateType("Appointment")
                    .aggregateId(String.valueOf(event.getAppointmentId()))
                    .eventType("AppointmentBookedNotification")
                    .payload(payload)
                    .status("PENDING")
                    .build();

            outboxEventRepository.save(outboxEvent);
            log.info("Saved outbox event for AppointmentBookedNotification {}", event.getAppointmentId());
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize AppointmentBookedEvent", e);
        }
    }

    @EventListener
    public void onAppointmentCancelled(AppointmentCancelledEvent event) {
        try {
            String payload = objectMapper.writeValueAsString(Map.of(
                    "appointmentId", event.getAppointmentId(),
                    "patientUserId", event.getPatientUserId(),
                    "doctorUserId", event.getDoctorUserId(),
                    "startTime", event.getStartTime().toString(),
                    "doctorName", event.getDoctorName()
            ));

            ClinicOutboxEvent outboxEvent = ClinicOutboxEvent.builder()
                    .aggregateType("Appointment")
                    .aggregateId(String.valueOf(event.getAppointmentId()))
                    .eventType("AppointmentCancelledNotification")
                    .payload(payload)
                    .status("PENDING")
                    .build();

            outboxEventRepository.save(outboxEvent);
            log.info("Saved outbox event for AppointmentCancelledNotification {}", event.getAppointmentId());
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize AppointmentCancelledEvent", e);
        }
    }
}
