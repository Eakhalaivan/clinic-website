package com.healthcare.clinic.laboratory.repository;

import com.healthcare.clinic.laboratory.entity.LabResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LabResultRepository extends JpaRepository<LabResult, Long> {
}
