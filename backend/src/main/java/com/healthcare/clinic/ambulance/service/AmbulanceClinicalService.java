package com.healthcare.clinic.ambulance.service;

import com.healthcare.clinic.ambulance.entity.EmergencyPatientRecord;
import com.healthcare.clinic.ambulance.repository.EmergencyPatientRecordRepository;
import com.healthcare.clinic.doctor.entity.ClinicOutboxEvent;
import com.healthcare.clinic.doctor.repository.ClinicOutboxEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AmbulanceClinicalService {
    private final EmergencyPatientRecordRepository recordRepository;
    private final ClinicOutboxEventRepository outboxEventRepository;

    @Transactional
    public EmergencyPatientRecord saveRecord(EmergencyPatientRecord record) {
        EmergencyPatientRecord saved = recordRepository.save(record);
        
        ClinicOutboxEvent outboxEvent = ClinicOutboxEvent.builder()
            .aggregateType("EmergencyPatientRecord")
            .aggregateId(saved.getId().toString())
            .eventType("AmbulanceHandoff")
            .payload("{\"recordId\": " + saved.getId() + "}")
            .status("PENDING")
            .build();
        outboxEventRepository.save(outboxEvent);
        
        return saved;
    }
}
