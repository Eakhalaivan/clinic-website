package com.healthcare.clinic.inventory.pharmacy.repository;

import com.healthcare.clinic.inventory.entity.InsuranceMedicineCoverage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("pharmacyInsuranceMedicineCoverageRepository")
public interface InsuranceMedicineCoverageRepository extends JpaRepository<InsuranceMedicineCoverage, String> {
    List<InsuranceMedicineCoverage> findByProviderProviderId(String providerId);
    Optional<InsuranceMedicineCoverage> findByProviderProviderIdAndMedicineId(String providerId, Long medicineId);
}
