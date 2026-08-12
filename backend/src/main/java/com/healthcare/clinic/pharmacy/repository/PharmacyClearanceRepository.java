package com.healthcare.clinic.pharmacy.repository;


import com.healthcare.clinic.pharmacy.entity.PharmacyClearance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PharmacyClearanceRepository extends JpaRepository<PharmacyClearance, Long> {
}
