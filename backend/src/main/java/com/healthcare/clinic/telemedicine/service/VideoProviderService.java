package com.healthcare.clinic.telemedicine.service;

import com.healthcare.clinic.telemedicine.entity.TeleconsultSession;
import com.healthcare.clinic.telemedicine.repository.TeleconsultSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VideoProviderService {
    
    private final TeleconsultSessionRepository repository;

    public TeleconsultSession createRoom(Long appointmentId, Long tenantId) {
        TeleconsultSession session = TeleconsultSession.builder()
            .appointmentId(appointmentId)
            .tenantId(tenantId)
            .providerType("MOCK_WEB_RTC")
            .roomId(UUID.randomUUID().toString())
            .patientToken(UUID.randomUUID().toString())
            .doctorToken(UUID.randomUUID().toString())
            .status("WAITING")
            .build();
        return repository.save(session);
    }
}
