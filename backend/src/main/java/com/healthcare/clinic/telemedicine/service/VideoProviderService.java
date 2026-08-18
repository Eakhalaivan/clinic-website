package com.healthcare.clinic.telemedicine.service;

import com.healthcare.clinic.telemedicine.entity.TeleconsultSession;
import com.healthcare.clinic.telemedicine.repository.TeleconsultSessionRepository;
import com.twilio.Twilio;
import com.twilio.jwt.accesstoken.AccessToken;
import com.twilio.jwt.accesstoken.VideoGrant;
import com.twilio.rest.video.v1.Room;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class VideoProviderService {
    
    private final TeleconsultSessionRepository repository;

    @Value("${twilio.video.enabled:false}")
    private boolean videoEnabled;

    @Value("${twilio.account-sid:mock}")
    private String accountSid;

    @Value("${twilio.api-key-sid:mock}")
    private String apiKeySid;

    @Value("${twilio.api-key-secret:mock}")
    private String apiKeySecret;

    @PostConstruct
    public void init() {
        if (videoEnabled && !accountSid.equals("mock") && !apiKeySid.equals("mock") && !apiKeySecret.equals("mock")) {
            Twilio.init(apiKeySid, apiKeySecret, accountSid);
            log.info("Twilio Video initialized");
        } else if (videoEnabled) {
            log.warn("Twilio Video enabled but missing credentials; falling back to MOCK");
            videoEnabled = false;
        }
    }

    public TeleconsultSession createRoom(Long appointmentId, Long tenantId) {
        return repository.findByAppointmentId(appointmentId)
            .orElseGet(() -> {
                String providerType = videoEnabled ? "TWILIO" : "MOCK_WEB_RTC";
                String roomId = UUID.randomUUID().toString();
                String patientToken = UUID.randomUUID().toString();
                String doctorToken = UUID.randomUUID().toString();

                if (videoEnabled) {
                    try {
                        // Create a real Twilio Video Room
                        Room twilioRoom = Room.creator()
                                .setUniqueName("consult-" + appointmentId + "-" + UUID.randomUUID().toString().substring(0,8))
                                .setType(Room.RoomType.GO)
                                .create();
                        
                        roomId = twilioRoom.getUniqueName();
                        
                        // Generate tokens for patient and doctor
                        patientToken = generateToken("patient-" + appointmentId, roomId);
                        doctorToken = generateToken("doctor-" + appointmentId, roomId);
                    } catch (Exception e) {
                        log.error("Failed to create Twilio room, falling back to MOCK", e);
                        providerType = "MOCK_WEB_RTC";
                    }
                }

                TeleconsultSession session = TeleconsultSession.builder()
                    .appointmentId(appointmentId)
                    .tenantId(tenantId)
                    .providerType(providerType)
                    .roomId(roomId)
                    .patientToken(patientToken)
                    .doctorToken(doctorToken)
                    .status("WAITING")
                    .build();
                return repository.save(session);
            });
    }

    public TeleconsultSession getSessionByAppointment(Long appointmentId) {
        return repository.findByAppointmentId(appointmentId)
            .orElseThrow(() -> new RuntimeException("Session not found"));
    }

    private String generateToken(String identity, String roomName) {
        VideoGrant grant = new VideoGrant().setRoom(roomName);
        AccessToken token = new AccessToken.Builder(accountSid, apiKeySid, apiKeySecret)
            .identity(identity)
            .grant(grant)
            .build();
        return token.toJwt();
    }
}
