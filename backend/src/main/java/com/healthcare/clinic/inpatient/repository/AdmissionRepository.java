package com.healthcare.clinic.inpatient.repository;

import com.healthcare.clinic.inpatient.entity.Admission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdmissionRepository extends JpaRepository<Admission, Long> {
    List<Admission> findByBranchId(Long branchId);
    List<Admission> findByBranchIdAndStatus(Long branchId, String status);
}
