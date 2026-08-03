package com.healthcare.clinic.inventory.pharmacy.repository;

import com.healthcare.clinic.inventory.entity.BarcodeScanLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("pharmacyBarcodeScanLogRepository")
public interface BarcodeScanLogRepository extends JpaRepository<BarcodeScanLog, String> {
}
