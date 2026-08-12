package com.healthcare.clinic.pharmacy.repository;


import com.healthcare.clinic.pharmacy.entity.InsuranceProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("pharmacyInsuranceProviderRepository")
public interface InsuranceProviderRepository extends JpaRepository<InsuranceProvider, String> {
    List<InsuranceProvider> findByActiveTrue();
}
