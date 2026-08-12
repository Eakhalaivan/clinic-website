package com.healthcare.clinic.pharmacy.repository;


import com.healthcare.clinic.pharmacy.entity.BatchReturnToSupplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("pharmacyBatchReturnToSupplierRepository")
public interface BatchReturnToSupplierRepository extends JpaRepository<BatchReturnToSupplier, String> {
}
