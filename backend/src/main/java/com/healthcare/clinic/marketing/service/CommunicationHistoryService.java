package com.healthcare.clinic.marketing.service;

import com.healthcare.clinic.marketing.entity.CommunicationHistory;
import com.healthcare.clinic.marketing.repository.CommunicationHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommunicationHistoryService {

    private final CommunicationHistoryRepository communicationHistoryRepository;

    @Transactional
    public CommunicationHistory sendCommunication(CommunicationHistory communication) {
        communication.setDirection("OUTBOUND");
        communication.setEventType("SENT");
        if (communication.getEventTimestamp() == null) {
            communication.setEventTimestamp(java.time.ZonedDateTime.now());
        }
        return communicationHistoryRepository.save(communication);
    }

    @Transactional(readOnly = true)
    public Page<CommunicationHistory> getPatientCommunicationHistory(Long patientId, Pageable pageable) {
        return communicationHistoryRepository.findByPatientIdOrderByEventTimestampDesc(patientId, pageable);
    }
}
