package com.healthcare.clinic.doctor.controller;

import com.healthcare.clinic.doctor.dto.DoctorWorkingHoursDto;
import com.healthcare.clinic.doctor.dto.ScheduleOverrideRequest;
import com.healthcare.clinic.doctor.dto.WorkingHoursRequest;
import com.healthcare.clinic.doctor.service.DoctorScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/doctors/{doctorId}")
@RequiredArgsConstructor
public class DoctorScheduleController {

    private final DoctorScheduleService scheduleService;

    @GetMapping("/working-hours")
    public ResponseEntity<List<DoctorWorkingHoursDto>> getWorkingHours(@PathVariable Long doctorId) {
        return ResponseEntity.ok(scheduleService.getWorkingHours(doctorId));
    }

    @PutMapping("/working-hours")
    @PreAuthorize("hasAuthority('ROLE_DOCTOR') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> setWorkingHours(
            @PathVariable Long doctorId,
            @RequestBody List<WorkingHoursRequest> requests) {
        com.healthcare.clinic.security.SecurityUtils.assertOwnerOrAdmin(doctorId);
        scheduleService.setWorkingHours(doctorId, requests);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/schedule-overrides")
    @PreAuthorize("hasAuthority('ROLE_DOCTOR') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> setScheduleOverride(
            @PathVariable Long doctorId,
            @RequestBody ScheduleOverrideRequest request) {
        com.healthcare.clinic.security.SecurityUtils.assertOwnerOrAdmin(doctorId);
        scheduleService.setOverride(doctorId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/schedule-overrides/{date}")
    @PreAuthorize("hasAuthority('ROLE_DOCTOR') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteScheduleOverride(
            @PathVariable Long doctorId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        com.healthcare.clinic.security.SecurityUtils.assertOwnerOrAdmin(doctorId);
        scheduleService.deleteOverride(doctorId, date);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/generate-slots")
    @PreAuthorize("hasAuthority('ROLE_DOCTOR') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Map<String, Integer>> generateSlots(
            @PathVariable Long doctorId,
            @RequestParam(defaultValue = "14") int days) {
        com.healthcare.clinic.security.SecurityUtils.assertOwnerOrAdmin(doctorId);
        LocalDate from = LocalDate.now();
        LocalDate to = from.plusDays(days);
        int count = scheduleService.generateSlotsForRange(doctorId, from, to);
        return ResponseEntity.ok(Map.of("slotsGenerated", count));
    }
}
