package com.healthcare.clinic.inventory.pharmacy.repository;

import com.healthcare.clinic.inventory.entity.BatchReturnToSupplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("pharmacyBatchReturnToSupplierRepository")
public interface BatchReturnToSupplierRepository extends JpaRepository<BatchReturnToSupplier, String> {
}
