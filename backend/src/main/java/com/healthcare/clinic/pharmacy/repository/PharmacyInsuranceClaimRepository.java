package com.healthcare.clinic.pharmacy.repository;

import com.healthcare.clinic.inventory.entity.BaseEntity;
import com.healthcare.clinic.inventory.entity.Patient;

import com.healthcare.clinic.pharmacy.entity.PharmacyInsuranceClaim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("pharmacyInsuranceClaimRepository")
public interface PharmacyInsuranceClaimRepository extends JpaRepository<PharmacyInsuranceClaim, String> {
    List<PharmacyInsuranceClaim> findByClaimStatus(String status);
}
