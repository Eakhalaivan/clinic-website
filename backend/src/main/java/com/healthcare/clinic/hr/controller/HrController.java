package com.healthcare.clinic.hr.controller;

import com.healthcare.clinic.hr.entity.Attendance;
import com.healthcare.clinic.hr.entity.Employee;
import com.healthcare.clinic.hr.entity.LeaveRequest;
import com.healthcare.clinic.hr.service.HrService;
import com.healthcare.clinic.identity.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/hr")
@RequiredArgsConstructor
@PreAuthorize("hasRole('HR') or hasRole('SUPER_ADMIN') or hasRole('ADMIN')")
public class HrController {

    private final HrService hrService;

    @GetMapping("/employees")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok(hrService.getAllEmployees());
    }

    @PostMapping("/employees")
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        return ResponseEntity.ok(hrService.createEmployee(employee));
    }

    @PostMapping("/attendance/check-in/{employeeId}")
    public ResponseEntity<Attendance> checkIn(@PathVariable Long employeeId) {
        return ResponseEntity.ok(hrService.checkIn(employeeId));
    }

    @PostMapping("/attendance/check-out/{employeeId}")
    public ResponseEntity<Attendance> checkOut(@PathVariable Long employeeId) {
        return ResponseEntity.ok(hrService.checkOut(employeeId));
    }

    @GetMapping("/attendance")
    public ResponseEntity<List<Attendance>> getAttendance(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        LocalDate targetDate = date != null ? date : LocalDate.now();
        return ResponseEntity.ok(hrService.getAttendanceByDate(targetDate));
    }

    @GetMapping("/leaves")
    public ResponseEntity<List<LeaveRequest>> getLeaves() {
        return ResponseEntity.ok(hrService.getAllLeaveRequests());
    }

    @PostMapping("/leaves")
    public ResponseEntity<LeaveRequest> submitLeave(@RequestBody LeaveRequest leaveRequest) {
        return ResponseEntity.ok(hrService.submitLeaveRequest(leaveRequest));
    }

    @PatchMapping("/leaves/{id}/status")
    public ResponseEntity<LeaveRequest> updateLeaveStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(hrService.updateLeaveStatus(id, status, user.getId()));
    }
}
