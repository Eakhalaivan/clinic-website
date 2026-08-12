package com.healthcare.clinic.patient.repository;

import com.healthcare.clinic.patient.entity.TeleconsultationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeleconsultationRequestRepository extends JpaRepository<TeleconsultationRequest, Long> {
    List<TeleconsultationRequest> findByPatientIdOrderByCreatedAtDesc(Long patientId);
}
