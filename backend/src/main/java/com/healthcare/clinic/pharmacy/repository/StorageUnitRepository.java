package com.healthcare.clinic.pharmacy.repository;


import com.healthcare.clinic.pharmacy.entity.StorageUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("pharmacyStorageUnitRepository")
public interface StorageUnitRepository extends JpaRepository<StorageUnit, String> {
}
