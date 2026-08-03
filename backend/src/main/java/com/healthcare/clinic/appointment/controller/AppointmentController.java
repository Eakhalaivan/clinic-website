package com.healthcare.clinic.appointment.controller;

import com.healthcare.clinic.appointment.entity.Appointment;
import com.healthcare.clinic.appointment.entity.AppointmentSlot;
import com.healthcare.clinic.appointment.service.AppointmentService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @GetMapping("/slots")
    public ResponseEntity<List<AppointmentSlot>> getAvailableSlots(
            @RequestParam Long doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime end) {
        
        List<AppointmentSlot> slots = appointmentService.getAvailableSlots(doctorId, start, end);
        return ResponseEntity.ok(slots);
    }

    @PostMapping("/book")
    @PreAuthorize("hasAuthority('ROLE_PATIENT')")
    public ResponseEntity<Appointment> bookAppointment(@jakarta.validation.Valid @RequestBody BookingRequest request) {
        Long currentUserId = com.healthcare.clinic.security.SecurityUtils.getCurrentUserId();
        Appointment appointment = appointmentService.bookAppointment(
                currentUserId, 
                request.getSlotId(), 
                request.getReasonForVisit());
                
        return ResponseEntity.ok(appointment);
    }

    @GetMapping("/patient/{userId}")
    @PreAuthorize("hasAuthority('ROLE_PATIENT') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<com.healthcare.clinic.appointment.dto.AppointmentResponseDto>> getAppointmentsForPatient(@PathVariable Long userId) {
        com.healthcare.clinic.security.SecurityUtils.assertOwnerOrAdmin(userId);
        return ResponseEntity.ok(appointmentService.getPatientAppointments(userId));
    }

    @GetMapping("/doctor/{userId}")
    @PreAuthorize("hasAuthority('ROLE_DOCTOR') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<com.healthcare.clinic.appointment.dto.AppointmentResponseDto>> getAppointmentsForDoctor(@PathVariable Long userId) {
        com.healthcare.clinic.security.SecurityUtils.assertOwnerOrAdmin(userId);
        return ResponseEntity.ok(appointmentService.getDoctorAppointments(userId));
    }

    @GetMapping("/today")
    @PreAuthorize("hasAuthority('ROLE_DOCTOR')")
    public ResponseEntity<List<com.healthcare.clinic.appointment.dto.AppointmentResponseDto>> getTodayAppointments(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime end) {
        Long currentUserId = com.healthcare.clinic.security.SecurityUtils.getCurrentUserId();
        
        if (start != null && end != null) {
            return ResponseEntity.ok(appointmentService.getAppointmentsInRange(currentUserId, start, end));
        } else {
            return ResponseEntity.ok(appointmentService.getTodayAppointments(currentUserId));
        }
    }

    @GetMapping("/doctor/me")
    @PreAuthorize("hasAuthority('ROLE_DOCTOR')")
    public ResponseEntity<List<com.healthcare.clinic.appointment.dto.AppointmentResponseDto>> getMyDoctorAppointments() {
        Long currentUserId = com.healthcare.clinic.security.SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(appointmentService.getDoctorAppointments(currentUserId));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAuthority('ROLE_DOCTOR') or hasAuthority('ROLE_RECEPTION') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> updateAppointmentStatus(@PathVariable Long id, @RequestParam String status) {
        appointmentService.updateAppointmentStatus(id, status);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/queue")
    public ResponseEntity<List<com.healthcare.clinic.appointment.dto.AppointmentResponseDto>> getAppointmentQueue() {
        return ResponseEntity.ok(List.of());
    }
}

@Data
class BookingRequest {
    
    @jakarta.validation.constraints.NotNull
    private Long slotId;
    
    @jakarta.validation.constraints.NotBlank
    @jakarta.validation.constraints.Size(max = 500)
    private String reasonForVisit;
}
