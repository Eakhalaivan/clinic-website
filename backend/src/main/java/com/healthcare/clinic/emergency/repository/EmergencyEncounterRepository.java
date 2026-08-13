package com.healthcare.clinic.emergency.repository;

import com.healthcare.clinic.emergency.entity.EmergencyEncounter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmergencyEncounterRepository extends JpaRepository<EmergencyEncounter, Long> {
    List<EmergencyEncounter> findByBranchIdOrderByArrivedAtDesc(Long branchId);
    List<EmergencyEncounter> findByBranchIdAndStatusOrderByArrivedAtDesc(Long branchId, String status);
}
