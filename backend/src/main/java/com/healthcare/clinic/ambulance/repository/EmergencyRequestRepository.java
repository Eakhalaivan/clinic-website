package com.healthcare.clinic.ambulance.repository;

import com.healthcare.clinic.ambulance.entity.EmergencyRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmergencyRequestRepository extends JpaRepository<EmergencyRequest, Long> {
    List<EmergencyRequest> findByStatus(String status);
    List<EmergencyRequest> findAllByOrderByRequestedAtDesc();
}
