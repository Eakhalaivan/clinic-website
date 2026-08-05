package com.healthcare.clinic.doctor.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class TelemedicineService {

    public String generateMeetingUrl(Long appointmentId) {
        // Here we simulate generating a secure meeting URL using an external provider like Jitsi Meet or Twilio.
        // For Jitsi, we can just return a unique URL based on the appointment ID.
        String roomName = "Clinic-Teleconsult-" + appointmentId + "-" + UUID.randomUUID().toString().substring(0, 8);
        String url = "https://meet.jit.si/" + roomName;
        log.info("Generated teleconsultation URL for appointment {}: {}", appointmentId, url);
        return url;
    }
}
