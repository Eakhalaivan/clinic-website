package com.healthcare.clinic.pharmacy.repository;

import com.healthcare.clinic.inventory.entity.BaseEntity;
import com.healthcare.clinic.inventory.entity.Patient;

import com.healthcare.clinic.pharmacy.entity.PrescriptionDispensed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionDispensedRepository extends JpaRepository<PrescriptionDispensed, Long> {
    List<PrescriptionDispensed> findByPharmacistId(Long pharmacistId);
}
