package com.healthcare.clinic.finance.repository;

import com.healthcare.clinic.finance.entity.TaxConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaxConfigurationRepository extends JpaRepository<TaxConfiguration, Long> {

    @Query("SELECT t FROM TaxConfiguration t WHERE t.isActive = true AND t.effectiveFrom <= :currentDate AND (t.effectiveTo IS NULL OR t.effectiveTo >= :currentDate)")
    List<TaxConfiguration> findActiveTaxes(LocalDate currentDate);
    
    @Query("SELECT t FROM TaxConfiguration t WHERE t.taxCode = :taxCode AND t.isActive = true AND t.effectiveFrom <= :currentDate AND (t.effectiveTo IS NULL OR t.effectiveTo >= :currentDate)")
    List<TaxConfiguration> findActiveTaxByCode(String taxCode, LocalDate currentDate);
}
