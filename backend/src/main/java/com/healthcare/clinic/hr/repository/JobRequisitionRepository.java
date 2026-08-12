package com.healthcare.clinic.hr.repository;

import com.healthcare.clinic.hr.entity.JobRequisition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRequisitionRepository extends JpaRepository<JobRequisition, Long> {
    List<JobRequisition> findByBranchId(Long branchId);
    List<JobRequisition> findByStatus(String status);
}
