package com.healthcare.clinic.patient.repository;

import com.healthcare.clinic.patient.entity.PatientPortalPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientPortalPaymentRepository extends JpaRepository<PatientPortalPayment, Long> {
    List<PatientPortalPayment> findByPatientIdOrderByPaymentDateDesc(Long patientId);
}
