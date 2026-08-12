package com.healthcare.clinic.hr.service;

import com.healthcare.clinic.hr.entity.*;
import com.healthcare.clinic.hr.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class HrBatchIntegrationTest {

    @Autowired
    private PayrollService payrollService;

    @Autowired
    private PayrollRunRepository payrollRunRepository;

    @Autowired
    private PayslipRepository payslipRepository;

    @Autowired
    private SalaryStructureRepository salaryStructureRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    public void testPayrollRunEndToEnd() {
        Employee employee = new Employee();
        employee.setUserId(999L);
        employee.setDepartment("Cardiology");
        employee.setDesignation("Senior Doctor");
        employee.setDateOfJoining(LocalDate.of(2020, 1, 1));
        employee = employeeRepository.save(employee);

        SalaryStructure structure = SalaryStructure.builder()
                .employee(employee)
                .basicSalary(new BigDecimal("5000.00"))
                .effectiveFrom(ZonedDateTime.now().minusDays(30))
                .status("ACTIVE")
                .build();
        salaryStructureRepository.save(structure);

        PayrollRun run = PayrollRun.builder()
                .name("May 2024 Payroll")
                .startDate(LocalDate.of(2024, 5, 1))
                .endDate(LocalDate.of(2024, 5, 31))
                .paymentDate(LocalDate.of(2024, 6, 1))
                .status("DRAFT")
                .build();
        run = payrollService.createPayrollRun(run);

        payrollService.processPayrollRun(run.getId());

        PayrollRun updatedRun = payrollRunRepository.findById(run.getId()).orElseThrow();
        assertThat(updatedRun.getStatus()).isEqualTo("REVIEW");

        long payslipCount = payslipRepository.findByPayrollRunId(run.getId()).size();
        assertThat(payslipCount).isGreaterThan(0);
    }
}
