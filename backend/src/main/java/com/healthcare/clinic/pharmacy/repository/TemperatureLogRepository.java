package com.healthcare.clinic.pharmacy.repository;


import com.healthcare.clinic.pharmacy.entity.TemperatureLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("pharmacyTemperatureLogRepository")
public interface TemperatureLogRepository extends JpaRepository<TemperatureLog, String> {
    List<TemperatureLog> findByStorageUnitUnitIdOrderByRecordedAtDesc(String unitId);
    List<TemperatureLog> findByBreachTrueOrderByRecordedAtDesc();
}
