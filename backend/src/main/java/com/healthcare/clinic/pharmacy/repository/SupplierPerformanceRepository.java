package com.healthcare.clinic.pharmacy.repository;


import com.healthcare.clinic.pharmacy.entity.SupplierPerformance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("pharmacySupplierPerformanceRepository")
public interface SupplierPerformanceRepository extends JpaRepository<SupplierPerformance, Long> {
    List<SupplierPerformance> findBySupplierIdOrderByPeriodStartDesc(Long supplierId);
    Optional<SupplierPerformance> findTopBySupplierIdOrderByPeriodEndDesc(Long supplierId);
}
