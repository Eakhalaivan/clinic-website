package com.healthcare.clinic.ambulance.repository;

import com.healthcare.clinic.ambulance.entity.AmbulanceAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AmbulanceAssignmentRepository extends JpaRepository<AmbulanceAssignment, Long> {
    List<AmbulanceAssignment> findByAmbulanceIdAndStatusIn(Long ambulanceId, List<String> statuses);
    Optional<AmbulanceAssignment> findByRequestId(Long requestId);
}
