package com.healthcare.clinic.doctor.repository;

import com.healthcare.clinic.doctor.entity.PrescriptionReconciliationMismatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrescriptionReconciliationMismatchRepository extends JpaRepository<PrescriptionReconciliationMismatch, Long> {
}
