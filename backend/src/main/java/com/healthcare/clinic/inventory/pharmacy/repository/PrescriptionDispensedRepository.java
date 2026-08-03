package com.healthcare.clinic.inventory.pharmacy.repository;

import com.healthcare.clinic.inventory.pharmacy.entity.PrescriptionDispensed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionDispensedRepository extends JpaRepository<PrescriptionDispensed, Long> {
    List<PrescriptionDispensed> findByPharmacistId(Long pharmacistId);
}
