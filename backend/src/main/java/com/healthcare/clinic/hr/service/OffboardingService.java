package com.healthcare.clinic.hr.service;

import com.healthcare.clinic.hr.entity.Employee;
import com.healthcare.clinic.hr.entity.OffboardingRequest;
import com.healthcare.clinic.hr.repository.EmployeeRepository;
import com.healthcare.clinic.hr.repository.OffboardingRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
public class OffboardingService {

    private final OffboardingRequestRepository requestRepository;
    private final EmployeeRepository employeeRepository;

    public OffboardingService(OffboardingRequestRepository requestRepository, EmployeeRepository employeeRepository) {
        this.requestRepository = requestRepository;
        this.employeeRepository = employeeRepository;
    }

    public OffboardingRequest initiateOffboarding(Long employeeId, String reason, LocalDate lastWorkingDay) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid employee ID"));

        if (requestRepository.findByEmployeeId(employeeId).isPresent()) {
            throw new IllegalStateException("Offboarding already initiated for this employee");
        }

        OffboardingRequest request = new OffboardingRequest();
        request.setEmployee(employee);
        request.setReason(reason);
        request.setLastWorkingDay(lastWorkingDay);
        request.setStatus("INITIATED");
        request.setClearanceChecklist("[{\"department\":\"HR\", \"cleared\":false}, {\"department\":\"IT\", \"cleared\":false}]");

        return requestRepository.save(request);
    }

    public OffboardingRequest updateOffboardingStatus(Long requestId, String status) {
        OffboardingRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid offboarding request ID"));

        request.setStatus(status);
        
        if ("COMPLETED".equals(status)) {
            Employee employee = request.getEmployee();
            employee.setIsActive(false);
            employeeRepository.save(employee);
        }

        return requestRepository.save(request);
    }
}
