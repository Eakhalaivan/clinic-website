package com.healthcare.clinic.hr.controller;

import com.healthcare.clinic.hr.entity.PayrollRun;
import com.healthcare.clinic.hr.entity.Payslip;
import com.healthcare.clinic.hr.repository.PayrollRunRepository;
import com.healthcare.clinic.hr.repository.PayslipRepository;
import com.healthcare.clinic.hr.service.PayrollService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hr/payroll")
@RequiredArgsConstructor
@PreAuthorize("hasRole('HR') or hasRole('SUPER_ADMIN') or hasRole('ADMIN')")
public class PayrollController {

    private final PayrollService payrollService;
    private final PayrollRunRepository payrollRunRepository;
    private final PayslipRepository payslipRepository;

    @GetMapping("/runs")
    public ResponseEntity<List<PayrollRun>> getAllPayrollRuns() {
        return ResponseEntity.ok(payrollRunRepository.findAll());
    }

    @PostMapping("/runs")
    public ResponseEntity<PayrollRun> createPayrollRun(@RequestBody PayrollRun run) {
        return ResponseEntity.ok(payrollService.createPayrollRun(run));
    }

    @PostMapping("/runs/{runId}/process")
    public ResponseEntity<Void> processPayrollRun(@PathVariable Long runId) {
        payrollService.processPayrollRun(runId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/runs/{runId}/payslips")
    public ResponseEntity<List<Payslip>> getPayslipsForRun(@PathVariable Long runId) {
        return ResponseEntity.ok(payslipRepository.findByPayrollRunId(runId));
    }

    @GetMapping("/payslips/employee/{employeeId}")
    public ResponseEntity<List<Payslip>> getEmployeePayslips(@PathVariable Long employeeId) {
        return ResponseEntity.ok(payslipRepository.findByEmployeeId(employeeId));
    }
}
