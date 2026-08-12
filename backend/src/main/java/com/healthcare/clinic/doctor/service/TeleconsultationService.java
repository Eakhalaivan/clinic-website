package com.healthcare.clinic.doctor.service;

import com.healthcare.clinic.doctor.entity.ClinicalEncounter;
import com.healthcare.clinic.doctor.entity.TeleconsultationSession;
import com.healthcare.clinic.doctor.repository.ClinicalEncounterRepository;
import com.healthcare.clinic.doctor.repository.TeleconsultationSessionRepository;
import com.healthcare.clinic.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.UUID;

@Service("doctorTeleconsultationService")
@RequiredArgsConstructor
@Slf4j
public class TeleconsultationService {

    private final TeleconsultationSessionRepository teleconsultationRepository;
    private final ClinicalEncounterRepository encounterRepository;

    @Transactional
    public TeleconsultationSession getOrCreateSessionForEncounter(Long encounterId) {
        return teleconsultationRepository.findByEncounterId(encounterId)
                .orElseGet(() -> createSession(encounterId));
    }

    private TeleconsultationSession createSession(Long encounterId) {
        ClinicalEncounter encounter = encounterRepository.findById(encounterId)
                .orElseThrow(() -> new ResourceNotFoundException("Encounter not found with id: " + encounterId));

        TeleconsultationSession session = new TeleconsultationSession();
        session.setEncounterId(encounterId);
        session.setPatientId(encounter.getPatientId());
        session.setDoctorId(encounter.getDoctorId());
        
        // Generate a mock secure room URL
        String roomId = UUID.randomUUID().toString();
        session.setRoomUrl("https://meet.clinic.com/room/" + roomId);
        
        session.setStatus("SCHEDULED");
        
        return teleconsultationRepository.save(session);
    }

    @Transactional
    public TeleconsultationSession updateStatus(Long sessionId, String status) {
        TeleconsultationSession session = teleconsultationRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Teleconsultation session not found: " + sessionId));

        session.setStatus(status);
        
        if ("IN_PROGRESS".equals(status)) {
            session.setStartedAt(ZonedDateTime.now());
        } else if ("COMPLETED".equals(status) || "CANCELLED".equals(status)) {
            session.setEndedAt(ZonedDateTime.now());
        }

        return teleconsultationRepository.save(session);
    }
}
