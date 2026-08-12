package com.healthcare.clinic.radiology.controller;

import com.healthcare.clinic.radiology.entity.RadiologyAppointment;
import com.healthcare.clinic.radiology.repository.RadiologyAppointmentRepository;
import com.healthcare.clinic.radiology.service.RadiologySchedulingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/radiology/modality")
@RequiredArgsConstructor
public class RadiologyModalityController {

    private final RadiologyAppointmentRepository appointmentRepository;
    private final RadiologySchedulingService schedulingService;

    @GetMapping("/worklist")
    @PreAuthorize("hasAnyRole('TECHNICIAN', 'ADMIN', 'RADIOLOGIST')")
    public ResponseEntity<List<RadiologyAppointment>> getWorklist(
            @RequestParam Long branchId,
            @RequestParam(required = false) String modality,
            @RequestParam(required = false) String date) {
        
        ZonedDateTime start = ZonedDateTime.parse(date != null ? date : ZonedDateTime.now().toLocalDate().atStartOfDay().atZone(java.time.ZoneId.systemDefault()).toString());
        ZonedDateTime end = start.plusDays(1);

        List<RadiologyAppointment> appointments = appointmentRepository.findByBranchIdAndScheduledTimeBetween(branchId, start, end);

        if (modality != null && !modality.isEmpty()) {
            appointments = appointments.stream()
                    .filter(a -> modality.equals(a.getModality()))
                    .collect(Collectors.toList());
        }

        return ResponseEntity.ok(appointments);
    }

    @PostMapping("/{appointmentId}/check-in")
    @PreAuthorize("hasAnyRole('RECEPTIONIST', 'TECHNICIAN', 'ADMIN')")
    public ResponseEntity<RadiologyAppointment> checkInPatient(@PathVariable Long appointmentId) {
        return ResponseEntity.ok(schedulingService.checkInPatient(appointmentId));
    }
}
