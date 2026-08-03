package com.healthcare.clinic.inventory.pharmacy.repository;

import com.healthcare.clinic.inventory.entity.StorageUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("pharmacyStorageUnitRepository")
public interface StorageUnitRepository extends JpaRepository<StorageUnit, String> {
}
