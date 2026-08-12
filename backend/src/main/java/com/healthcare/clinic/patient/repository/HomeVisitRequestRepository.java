package com.healthcare.clinic.patient.repository;

import com.healthcare.clinic.patient.entity.HomeVisitRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HomeVisitRequestRepository extends JpaRepository<HomeVisitRequest, Long> {
    List<HomeVisitRequest> findByPatientIdOrderByCreatedAtDesc(Long patientId);
}
