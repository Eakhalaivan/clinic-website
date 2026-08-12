package com.healthcare.clinic.hr.repository;

import com.healthcare.clinic.hr.entity.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    List<JobApplication> findByJobRequisitionId(Long jobRequisitionId);
    List<JobApplication> findByApplicantEmail(String email);
}
