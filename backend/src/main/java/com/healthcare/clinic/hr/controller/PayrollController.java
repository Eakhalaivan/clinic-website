package com.healthcare.clinic.hr.controller;

import com.healthcare.clinic.hr.entity.StaffPayroll;
import com.healthcare.clinic.hr.repository.StaffPayrollRepository;
import com.healthcare.clinic.identity.entity.User;
import com.healthcare.clinic.identity.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/hr/payroll")
@RequiredArgsConstructor
public class PayrollController {

    private final StaffPayrollRepository payrollRepository;
    private final UserRepository userRepository;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_HR') or hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPER_ADMIN')")
    public ResponseEntity<List<StaffPayroll>> getAllPayroll(@RequestParam(required = false) String monthYear) {
        if (monthYear != null) {
            return ResponseEntity.ok(payrollRepository.findByMonthYear(monthYear));
        }
        return ResponseEntity.ok(payrollRepository.findAll());
    }

    @PostMapping("/generate")
    @PreAuthorize("hasAuthority('ROLE_HR') or hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPER_ADMIN')")
    public ResponseEntity<StaffPayroll> generatePayroll(@RequestBody GeneratePayrollRequest request) {
        User staff = userRepository.findById(request.getStaffId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Staff not found"));

        if (payrollRepository.findByStaffIdAndMonthYear(staff.getId(), request.getMonthYear()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payroll already generated for this month");
        }

        BigDecimal netSalary = request.getBasicSalary()
                .add(request.getAllowances())
                .subtract(request.getDeductions());

        StaffPayroll payroll = StaffPayroll.builder()
                .staff(staff)
                .monthYear(request.getMonthYear())
                .basicSalary(request.getBasicSalary())
                .allowances(request.getAllowances())
                .deductions(request.getDeductions())
                .netSalary(netSalary)
                .status("PENDING")
                .build();

        return ResponseEntity.ok(payrollRepository.save(payroll));
    }

    @PutMapping("/{payrollId}/process")
    @PreAuthorize("hasAuthority('ROLE_HR') or hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPER_ADMIN')")
    public ResponseEntity<StaffPayroll> processPayment(@PathVariable Long payrollId) {
        StaffPayroll payroll = payrollRepository.findById(payrollId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payroll not found"));

        if ("PAID".equals(payroll.getStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payroll is already paid");
        }

        payroll.setStatus("PAID");
        return ResponseEntity.ok(payrollRepository.save(payroll));
    }

    @Data
    public static class GeneratePayrollRequest {
        private Long staffId;
        private String monthYear;
        private BigDecimal basicSalary;
        private BigDecimal allowances = BigDecimal.ZERO;
        private BigDecimal deductions = BigDecimal.ZERO;
    }
}
