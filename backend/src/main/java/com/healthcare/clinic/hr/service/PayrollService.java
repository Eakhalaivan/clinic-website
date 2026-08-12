package com.healthcare.clinic.hr.service;

import com.healthcare.clinic.hr.entity.*;
import com.healthcare.clinic.hr.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PayrollService {

    private final PayrollRunRepository payrollRunRepository;
    private final PayslipRepository payslipRepository;
    private final SalaryStructureRepository salaryStructureRepository;
    private final TaxService taxService;
    private final EmployeeRepository employeeRepository;

    public PayrollService(PayrollRunRepository payrollRunRepository, 
                          PayslipRepository payslipRepository, 
                          SalaryStructureRepository salaryStructureRepository, 
                          TaxService taxService,
                          EmployeeRepository employeeRepository) {
        this.payrollRunRepository = payrollRunRepository;
        this.payslipRepository = payslipRepository;
        this.salaryStructureRepository = salaryStructureRepository;
        this.taxService = taxService;
        this.employeeRepository = employeeRepository;
    }

    public PayrollRun createPayrollRun(PayrollRun run) {
        return payrollRunRepository.save(run);
    }

    public void processPayrollRun(Long runId) {
        PayrollRun run = payrollRunRepository.findById(runId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid payroll run ID"));

        run.setStatus("PROCESSING");
        payrollRunRepository.save(run);

        List<Employee> employees = employeeRepository.findAll();

        for (Employee employee : employees) {
            salaryStructureRepository.findByEmployeeIdAndStatus(employee.getId(), "ACTIVE").ifPresent(structure -> {
                generatePayslip(employee, structure, run);
            });
        }

        run.setStatus("REVIEW");
        payrollRunRepository.save(run);
    }

    private void generatePayslip(Employee employee, SalaryStructure structure, PayrollRun run) {
        BigDecimal basic = structure.getBasicSalary();
        BigDecimal totalAllowances = BigDecimal.ZERO;
        BigDecimal totalDeductions = BigDecimal.ZERO;
        
        Map<String, Object> breakdown = new HashMap<>();
        Map<String, BigDecimal> allowancesMap = new HashMap<>();
        Map<String, BigDecimal> deductionsMap = new HashMap<>();

        if (structure.getComponents() != null) {
            for (SalaryComponent comp : structure.getComponents()) {
                BigDecimal amount = comp.getAmountType().equals("FIXED") ? 
                        comp.getValue() : 
                        basic.multiply(comp.getValue()).divide(BigDecimal.valueOf(100));

                if (comp.getType().equals("ALLOWANCE")) {
                    totalAllowances = totalAllowances.add(amount);
                    allowancesMap.put(comp.getName(), amount);
                } else {
                    totalDeductions = totalDeductions.add(amount);
                    deductionsMap.put(comp.getName(), amount);
                }
            }
        }

        // Apply dynamic tax logic
        BigDecimal tax = taxService.calculateTax(basic.add(totalAllowances));
        totalDeductions = totalDeductions.add(tax);
        deductionsMap.put("Income Tax", tax);

        breakdown.put("allowances", allowancesMap);
        breakdown.put("deductions", deductionsMap);

        BigDecimal netPay = basic.add(totalAllowances).subtract(totalDeductions);

        Payslip payslip = Payslip.builder()
                .employee(employee)
                .payrollRun(run)
                .basicPay(basic)
                .totalAllowances(totalAllowances)
                .totalDeductions(totalDeductions)
                .netPay(netPay)
                .breakdown(breakdown)
                .build();
        
        payslipRepository.save(payslip);
    }
}
