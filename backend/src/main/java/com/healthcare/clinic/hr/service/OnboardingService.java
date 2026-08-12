package com.healthcare.clinic.hr.service;

import com.healthcare.clinic.hr.entity.Employee;
import com.healthcare.clinic.hr.entity.OnboardingChecklist;
import com.healthcare.clinic.hr.repository.EmployeeRepository;
import com.healthcare.clinic.hr.repository.OnboardingChecklistRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OnboardingService {

    private final OnboardingChecklistRepository checklistRepository;
    private final EmployeeRepository employeeRepository;

    public OnboardingService(OnboardingChecklistRepository checklistRepository, EmployeeRepository employeeRepository) {
        this.checklistRepository = checklistRepository;
        this.employeeRepository = employeeRepository;
    }

    public OnboardingChecklist initiateOnboarding(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid employee ID"));

        if (checklistRepository.findByEmployeeId(employeeId).isPresent()) {
            throw new IllegalStateException("Onboarding already initiated for this employee");
        }

        OnboardingChecklist checklist = new OnboardingChecklist();
        checklist.setEmployee(employee);
        checklist.setStatus("IN_PROGRESS");
        checklist.setTasks("[{\"taskName\":\"HR Documents\", \"isCompleted\":false}, {\"taskName\":\"IT Setup\", \"isCompleted\":false}]");
        
        return checklistRepository.save(checklist);
    }

    public OnboardingChecklist updateChecklistStatus(Long checklistId, String status) {
        OnboardingChecklist checklist = checklistRepository.findById(checklistId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid checklist ID"));
        
        checklist.setStatus(status);
        return checklistRepository.save(checklist);
    }
}
