package com.healthcare.clinic.hr.service;

import com.healthcare.clinic.hr.entity.Attendance;
import com.healthcare.clinic.hr.repository.AttendanceRepository;
import com.healthcare.clinic.hr.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Optional;

@Service
@Transactional
public class BiometricSyncService {

    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;

    public BiometricSyncService(AttendanceRepository attendanceRepository, EmployeeRepository employeeRepository) {
        this.attendanceRepository = attendanceRepository;
        this.employeeRepository = employeeRepository;
    }

    public void processBiometricPunch(Long employeeId, ZonedDateTime timestamp, String direction) {
        LocalDate date = timestamp.toLocalDate();

        Optional<Attendance> optionalAttendance = attendanceRepository.findByEmployeeIdAndDate(employeeId, date);
        Attendance attendance;
        if (optionalAttendance.isPresent()) {
            attendance = optionalAttendance.get();
        } else {
            attendance = new Attendance();
            attendance.setEmployee(employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid employee ID")));
            attendance.setDate(date);
        }

        if ("IN".equalsIgnoreCase(direction)) {
            if (attendance.getCheckIn() == null || timestamp.isBefore(attendance.getCheckIn())) {
                attendance.setCheckIn(timestamp);
            }
        } else if ("OUT".equalsIgnoreCase(direction)) {
            if (attendance.getCheckOut() == null || timestamp.isAfter(attendance.getCheckOut())) {
                attendance.setCheckOut(timestamp);
            }
        }

        attendanceRepository.save(attendance);
    }
}
