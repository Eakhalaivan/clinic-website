package com.healthcare.clinic.hr.repository;

import com.healthcare.clinic.hr.entity.SalaryStructure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SalaryStructureRepository extends JpaRepository<SalaryStructure, Long> {
    Optional<SalaryStructure> findByEmployeeIdAndStatus(Long employeeId, String status);
}
