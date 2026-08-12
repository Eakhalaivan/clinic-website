package com.healthcare.clinic.laboratory.repository;

import com.healthcare.clinic.laboratory.entity.LabResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LabResultRepository extends JpaRepository<LabResult, Long> {
    Optional<LabResult> findByRequestId(Long requestId);
}
