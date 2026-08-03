package com.healthcare.clinic.inventory.pharmacy.repository;

import com.healthcare.clinic.inventory.entity.InsuranceProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("pharmacyInsuranceProviderRepository")
public interface InsuranceProviderRepository extends JpaRepository<InsuranceProvider, String> {
    List<InsuranceProvider> findByActiveTrue();
}
