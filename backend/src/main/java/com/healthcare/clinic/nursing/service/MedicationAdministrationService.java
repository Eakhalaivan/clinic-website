package com.healthcare.clinic.nursing.service;

import com.healthcare.clinic.nursing.entity.MedicationAdministrationRecord;
import com.healthcare.clinic.nursing.entity.MarStatus;
import com.healthcare.clinic.nursing.repository.MedicationAdministrationRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicationAdministrationService {
    
    private final MedicationAdministrationRecordRepository marRepository;
    
    public List<MedicationAdministrationRecord> getAllRecords() {
        return marRepository.findAll();
    }
    
    public MedicationAdministrationRecord createRecord(MedicationAdministrationRecord record) {
        if(record.getStatus() == null) record.setStatus(MarStatus.DUE);
        return marRepository.save(record);
    }
    
    public MedicationAdministrationRecord markAsGiven(Long id, Long nurseId) {
        MedicationAdministrationRecord record = marRepository.findById(id).orElseThrow(() -> new RuntimeException("MAR not found"));
        record.setStatus(MarStatus.GIVEN);
        record.setAdministeredAt(LocalDateTime.now());
        record.setAdministeredByUserId(nurseId);
        return marRepository.save(record);
    }
}
