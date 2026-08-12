package com.healthcare.clinic.pharmacy.repository;


import com.healthcare.clinic.pharmacy.entity.PrescriptionDispensed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrescriptionDispensedRepository extends JpaRepository<PrescriptionDispensed, Long> {
    Optional<PrescriptionDispensed> findByIdempotencyKey(String idempotencyKey);
    List<PrescriptionDispensed> findByPharmacistId(Long pharmacistId);
}
