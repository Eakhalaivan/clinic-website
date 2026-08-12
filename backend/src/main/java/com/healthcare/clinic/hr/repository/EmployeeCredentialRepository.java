package com.healthcare.clinic.hr.repository;

import com.healthcare.clinic.hr.entity.EmployeeCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EmployeeCredentialRepository extends JpaRepository<EmployeeCredential, Long> {
    List<EmployeeCredential> findByEmployeeId(Long employeeId);
    List<EmployeeCredential> findByExpiryDateBeforeAndStatus(LocalDate date, String status);
}
