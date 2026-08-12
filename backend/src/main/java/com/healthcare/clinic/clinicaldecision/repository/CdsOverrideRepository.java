package com.healthcare.clinic.clinicaldecision.repository;

import com.healthcare.clinic.clinicaldecision.entity.CdsOverride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CdsOverrideRepository extends JpaRepository<CdsOverride, Long> {
    List<CdsOverride> findByPrescriptionId(Long prescriptionId);
}
