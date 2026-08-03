package com.healthcare.clinic.inventory.pharmacy.repository;

import com.healthcare.clinic.inventory.entity.TemperatureLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("pharmacyTemperatureLogRepository")
public interface TemperatureLogRepository extends JpaRepository<TemperatureLog, String> {
    List<TemperatureLog> findByStorageUnitUnitIdOrderByRecordedAtDesc(String unitId);
    List<TemperatureLog> findByBreachTrueOrderByRecordedAtDesc();
}
