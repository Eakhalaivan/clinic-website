package com.healthcare.clinic.hr.service;

import com.healthcare.clinic.hr.entity.JobApplication;
import com.healthcare.clinic.hr.entity.JobRequisition;
import com.healthcare.clinic.hr.repository.JobApplicationRepository;
import com.healthcare.clinic.hr.repository.JobRequisitionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RecruitmentService {

    private final JobRequisitionRepository requisitionRepository;
    private final JobApplicationRepository applicationRepository;

    public RecruitmentService(JobRequisitionRepository requisitionRepository, JobApplicationRepository applicationRepository) {
        this.requisitionRepository = requisitionRepository;
        this.applicationRepository = applicationRepository;
    }

    public JobRequisition createRequisition(JobRequisition requisition) {
        return requisitionRepository.save(requisition);
    }

    public List<JobRequisition> getActiveRequisitions() {
        return requisitionRepository.findByStatus("POSTED");
    }

    public JobApplication applyForJob(Long requisitionId, JobApplication application) {
        JobRequisition requisition = requisitionRepository.findById(requisitionId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid requisition ID"));
        
        List<JobApplication> existingApps = applicationRepository.findByApplicantEmail(application.getApplicantEmail());
        if (existingApps.stream().anyMatch(app -> app.getJobRequisition().getId().equals(requisitionId))) {
            throw new IllegalStateException("Applicant has already applied for this position");
        }

        application.setJobRequisition(requisition);
        application.setStatus("APPLIED");
        return applicationRepository.save(application);
    }

    public JobApplication updateApplicationStatus(Long applicationId, String status) {
        JobApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid application ID"));
        application.setStatus(status);
        return applicationRepository.save(application);
    }
}
