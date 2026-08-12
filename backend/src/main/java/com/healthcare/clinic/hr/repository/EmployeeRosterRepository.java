package com.healthcare.clinic.hr.repository;

import com.healthcare.clinic.hr.entity.EmployeeRoster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EmployeeRosterRepository extends JpaRepository<EmployeeRoster, Long> {
    List<EmployeeRoster> findByBranchIdAndRosterDateBetween(Long branchId, LocalDate startDate, LocalDate endDate);
    List<EmployeeRoster> findByEmployeeIdAndRosterDate(Long employeeId, LocalDate date);
}
