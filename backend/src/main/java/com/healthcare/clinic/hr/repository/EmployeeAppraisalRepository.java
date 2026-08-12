package com.healthcare.clinic.hr.repository;

import com.healthcare.clinic.hr.entity.EmployeeAppraisal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeAppraisalRepository extends JpaRepository<EmployeeAppraisal, Long> {
    List<EmployeeAppraisal> findByEmployeeId(Long employeeId);
    List<EmployeeAppraisal> findByReviewerId(Long reviewerId);
}
