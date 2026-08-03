package com.healthcare.clinic.hr.repository;

import com.healthcare.clinic.hr.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByUserId(Long userId);
    List<Employee> findByIsActiveTrue();
    List<Employee> findByBranchId(Long branchId);
}
