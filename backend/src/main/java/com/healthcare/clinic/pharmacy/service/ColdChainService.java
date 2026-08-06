package com.healthcare.clinic.pharmacy.service;

import com.healthcare.clinic.inventory.entity.BaseEntity;
import com.healthcare.clinic.inventory.entity.Patient;

import com.healthcare.clinic.pharmacy.entity.StorageUnit;
import com.healthcare.clinic.pharmacy.entity.TemperatureLog;
import com.healthcare.clinic.pharmacy.repository.StorageUnitRepository;
import com.healthcare.clinic.pharmacy.repository.TemperatureLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service("pharmacyColdChainService")
public class ColdChainService {

    private final StorageUnitRepository unitRepository;
    private final TemperatureLogRepository logRepository;

    public ColdChainService(StorageUnitRepository unitRepository,
                             TemperatureLogRepository logRepository) {
        this.unitRepository = unitRepository;
        this.logRepository = logRepository;
    }

    public List<StorageUnit> getStorageUnits() {
        return unitRepository.findAll();
    }

    @Transactional
    public StorageUnit createStorageUnit(StorageUnit unit) {
        unit.setUnitId(java.util.UUID.randomUUID().toString());
        return unitRepository.save(unit);
    }

    @Transactional
    public TemperatureLog recordTemperature(TemperatureLog log) {
        log.setLogId(java.util.UUID.randomUUID().toString());
        log.setRecordedAt(LocalDateTime.now());
        
        // Manual check for breach if generated value doesn't compile dynamically
        boolean isBreach = log.getRecordedTemperature().compareTo(log.getMinThreshold()) < 0 || 
                           log.getRecordedTemperature().compareTo(log.getMaxThreshold()) > 0;
        
        if (isBreach) {
            log.setBreachSeverity("critical");
        }
        
        return logRepository.save(log);
    }

    @Transactional
    public TemperatureLog recordCorrectiveAction(String logId, String action, Long userId) {
        TemperatureLog log = logRepository.findById(logId)
                .orElseThrow(() -> new RuntimeException("Log not found"));
        log.setCorrectiveAction(action);
        log.setCorrectiveActionBy(userId);
        log.setCorrectiveActionAt(LocalDateTime.now());
        return logRepository.save(log);
    }

    public List<TemperatureLog> getLogsByUnit(String unitId) {
        return logRepository.findByStorageUnitUnitIdOrderByRecordedAtDesc(unitId);
    }

    public List<TemperatureLog> getBreachLogs() {
        return logRepository.findByBreachTrueOrderByRecordedAtDesc();
    }
}
