package com.healthcare.clinic.hr.service;

import com.healthcare.clinic.hr.entity.EmployeeAppraisal;
import com.healthcare.clinic.hr.repository.EmployeeAppraisalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PerformanceService {

    private final EmployeeAppraisalRepository appraisalRepository;

    public PerformanceService(EmployeeAppraisalRepository appraisalRepository) {
        this.appraisalRepository = appraisalRepository;
    }

    public EmployeeAppraisal createAppraisal(EmployeeAppraisal appraisal) {
        return appraisalRepository.save(appraisal);
    }

    public List<EmployeeAppraisal> getEmployeeAppraisals(Long employeeId) {
        return appraisalRepository.findByEmployeeId(employeeId);
    }

    public EmployeeAppraisal submitSelfReview(Long appraisalId, String comments) {
        EmployeeAppraisal appraisal = appraisalRepository.findById(appraisalId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid appraisal ID"));
        
        appraisal.setComments(comments);
        appraisal.setStatus("MANAGER_REVIEW");
        return appraisalRepository.save(appraisal);
    }

    public EmployeeAppraisal submitManagerReview(Long appraisalId, Integer rating) {
        EmployeeAppraisal appraisal = appraisalRepository.findById(appraisalId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid appraisal ID"));
        
        appraisal.setOverallRating(rating);
        appraisal.setStatus("COMPLETED");
        return appraisalRepository.save(appraisal);
    }
}
