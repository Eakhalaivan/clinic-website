package com.healthcare.clinic.emergency.repository;

import com.healthcare.clinic.emergency.entity.EmergencyOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmergencyOrderRepository extends JpaRepository<EmergencyOrder, Long> {
    List<EmergencyOrder> findByEmergencyEncounterId(Long emergencyEncounterId);
}
