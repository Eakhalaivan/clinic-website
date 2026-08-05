package com.healthcare.clinic.inventory.pharmacy.repository;

import com.healthcare.clinic.inventory.pharmacy.entity.PharmacyClearance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PharmacyClearanceRepository extends JpaRepository<PharmacyClearance, Long> {
}
