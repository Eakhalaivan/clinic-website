package com.healthcare.clinic.hr.service;

import com.healthcare.clinic.hr.entity.Attendance;
import com.healthcare.clinic.hr.repository.AttendanceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    public AttendanceService(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    public Attendance requestRegularization(Long attendanceId, String reason) {
        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid attendance ID"));
        
        attendance.setRegularizationReason(reason);
        attendance.setRegularizationStatus("PENDING");
        return attendanceRepository.save(attendance);
    }

    public Attendance approveRegularization(Long attendanceId, Long approverId) {
        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid attendance ID"));
        
        attendance.setRegularizationStatus("APPROVED");
        attendance.setApprovedBy(approverId);
        return attendanceRepository.save(attendance);
    }
}
