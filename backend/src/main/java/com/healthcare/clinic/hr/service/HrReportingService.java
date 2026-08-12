package com.healthcare.clinic.hr.service;

import com.healthcare.clinic.hr.repository.EmployeeRepository;
import com.healthcare.clinic.hr.repository.PayrollRunRepository;
import com.healthcare.clinic.hr.repository.PayslipRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class HrReportingService {

    private final EmployeeRepository employeeRepository;
    private final PayrollRunRepository payrollRunRepository;
    private final PayslipRepository payslipRepository;

    public HrReportingService(EmployeeRepository employeeRepository, 
                              PayrollRunRepository payrollRunRepository, 
                              PayslipRepository payslipRepository) {
        this.employeeRepository = employeeRepository;
        this.payrollRunRepository = payrollRunRepository;
        this.payslipRepository = payslipRepository;
    }

    public Map<String, Object> getHrDashboardMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        
        long totalEmployees = employeeRepository.count();
        long activeEmployees = employeeRepository.findAll().stream()
                .filter(e -> Boolean.TRUE.equals(e.getIsActive()))
                .count();

        metrics.put("totalHeadcount", totalEmployees);
        metrics.put("activeEmployees", activeEmployees);
        // Can add more metrics like pending onboarding, active offboarding, etc.

        return metrics;
    }
}
