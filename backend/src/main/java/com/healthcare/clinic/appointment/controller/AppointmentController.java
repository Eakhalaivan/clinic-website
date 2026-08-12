package com.healthcare.clinic.appointment.controller;

import com.healthcare.clinic.appointment.entity.Appointment;
import com.healthcare.clinic.appointment.entity.AppointmentSlot;
import com.healthcare.clinic.appointment.service.AppointmentService;
import com.healthcare.clinic.appointment.entity.AppointmentStatus;
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
    @PreAuthorize("hasAuthority('ROLE_DOCTOR') or hasAuthority('ROLE_RECEPTION') or hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_NURSE')")
    public ResponseEntity<Void> updateAppointmentStatus(@PathVariable Long id, @RequestParam AppointmentStatus status) {
        appointmentService.updateAppointmentStatus(id, status);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasAuthority('ROLE_DOCTOR') or hasAuthority('ROLE_RECEPTION') or hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_PATIENT')")
    public ResponseEntity<Void> cancelAppointment(@PathVariable Long id, @RequestParam String reason) {
        assertCanAccessAppointment(id);
        appointmentService.cancelAppointment(id, reason);
        return ResponseEntity.ok().build();
    }
    
    @PatchMapping("/{id}/reschedule")
    @PreAuthorize("hasAuthority('ROLE_RECEPTION') or hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_PATIENT')")
    public ResponseEntity<Appointment> rescheduleAppointment(@PathVariable Long id, @RequestParam Long newSlotId) {
        assertCanAccessAppointment(id);
        Appointment newAppt = appointmentService.rescheduleAppointment(id, newSlotId);
        return ResponseEntity.ok(newAppt);
    }

    @GetMapping("/queue")
    @PreAuthorize("hasAuthority('ROLE_DOCTOR') or hasAuthority('ROLE_RECEPTION') or hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_NURSE')")
    public ResponseEntity<List<com.healthcare.clinic.appointment.dto.AppointmentResponseDto>> getAppointmentQueue() {
        Long currentUserId = com.healthcare.clinic.security.SecurityUtils.getCurrentUserId();
        // Since we don't have the user's role here trivially without injecting something else, 
        // we'll just return all today's appointments for the entire branch/clinic, assuming a single branch for now,
        // or we can fetch by doctor if it's a doctor. For simplicity and as required, returning today's queue.
        // I will need to add `getAllTodayAppointments()` to `AppointmentService`. Let's add it in the next step.
        return ResponseEntity.ok(appointmentService.getAllTodayAppointments());
    }

    private void assertCanAccessAppointment(Long id) {
        org.springframework.security.core.Authentication auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        boolean hasPrivilegedRole = auth.getAuthorities().stream().anyMatch(a -> 
            a.getAuthority().equals("ROLE_ADMIN") || 
            a.getAuthority().equals("ROLE_RECEPTION") ||
            a.getAuthority().equals("ROLE_DOCTOR") ||
            a.getAuthority().equals("ROLE_NURSE") ||
            a.getAuthority().equals("ROLE_SUPER_ADMIN")
        );
        if (hasPrivilegedRole) {
            return;
        }
        
        Long currentUserId = com.healthcare.clinic.security.SecurityUtils.getCurrentUserId();
        Appointment appointment = appointmentService.getAppointmentById(id);
        if (currentUserId == null || (appointment.getPatient() != null && !currentUserId.equals(appointment.getPatient().getUserId()))) {
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.FORBIDDEN, "Not authorized to access this appointment");
        }
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
