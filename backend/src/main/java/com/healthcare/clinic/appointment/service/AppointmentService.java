package com.healthcare.clinic.appointment.service;

import com.healthcare.clinic.appointment.entity.Appointment;
import com.healthcare.clinic.appointment.entity.AppointmentSlot;
import com.healthcare.clinic.doctor.entity.DoctorProfile;
import com.healthcare.clinic.patient.entity.PatientProfile;
import com.healthcare.clinic.appointment.repository.AppointmentRepository;
import com.healthcare.clinic.appointment.repository.AppointmentSlotRepository;
import com.healthcare.clinic.doctor.repository.DoctorProfileRepository;
import com.healthcare.clinic.patient.repository.PatientProfileRepository;
import com.healthcare.clinic.identity.repository.UserRepository;
import com.healthcare.clinic.identity.entity.User;
import com.healthcare.clinic.reception.repository.QueueTokenRepository;
import com.healthcare.clinic.reception.entity.QueueToken;
import com.healthcare.clinic.branch.repository.BranchRepository;
import com.healthcare.clinic.branch.entity.Branch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import com.healthcare.clinic.appointment.entity.AppointmentStatus;
import com.healthcare.clinic.appointment.event.AppointmentBookedEvent;
import com.healthcare.clinic.appointment.event.AppointmentStatusChangedEvent;
import com.healthcare.clinic.notification.event.AppointmentCancelledEvent;
import com.healthcare.clinic.appointment.event.AppointmentCompletedEvent;
import org.springframework.context.ApplicationEventPublisher;


@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentService {

    private final AppointmentSlotRepository slotRepository;
    private final AppointmentRepository appointmentRepository;
    private final PatientProfileRepository patientRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final UserRepository userRepository;
    private final QueueTokenRepository queueTokenRepository;
    private final BranchRepository branchRepository;

    @Transactional(readOnly = true)
    public List<AppointmentSlot> getAvailableSlots(Long doctorId, ZonedDateTime start, ZonedDateTime end) {
        ZonedDateTime now = ZonedDateTime.now();
        return slotRepository.findByDoctorUserIdAndStartTimeBetweenAndIsBookedFalse(doctorId, start, end).stream()
                .filter(slot -> slot.getStartTime().isAfter(now))
                .toList();
    }

    @Transactional
    public Appointment bookAppointment(Long patientUserId, Long slotId, String reasonForVisit) {
        // Auto-create a minimal PatientProfile if one doesn't exist yet (new patient registration flow)
        PatientProfile patient = patientRepository.findByUserId(patientUserId)
                .orElseGet(() -> {
                    log.info("No PatientProfile found for user ID: {}. Auto-creating a minimal profile.", patientUserId);
                    PatientProfile newProfile = PatientProfile.builder()
                            .userId(patientUserId)
                            .emergencyContactName("Not provided")
                            .emergencyContactPhone("+10000000000")
                            .branchId(1L)
                            .build();
                    return patientRepository.save(newProfile);
                });

        AppointmentSlot slot = slotRepository.findByIdWithLock(slotId)
                .orElseThrow(() -> new RuntimeException("Slot not found: " + slotId));

        if (slot.getIsBooked()) {
            throw new RuntimeException("Slot is already booked.");
        }

        java.time.DayOfWeek day = slot.getStartTime().getDayOfWeek();
        if (day == java.time.DayOfWeek.SATURDAY || day == java.time.DayOfWeek.SUNDAY) {
            throw new IllegalArgumentException("Cannot book appointments on weekends.");
        }

        // Optimistic locking handles concurrent modifications to the slot
        slot.setIsBooked(true);
        slotRepository.save(slot);

        Appointment appointment = Appointment.builder()
                .patient(patient)
                .doctor(slot.getDoctor())
                .slot(slot)
                .status(AppointmentStatus.BOOKED)
                .reasonForVisit(reasonForVisit)
                .branchId(slot.getBranchId())
                .build();

        Appointment savedAppointment = appointmentRepository.save(appointment);

        User patientUser = userRepository.findById(patient.getUserId())
                .orElseThrow(() -> new RuntimeException("Patient user not found"));
        User doctorUser = userRepository.findById(slot.getDoctor().getUserId())
                .orElseThrow(() -> new RuntimeException("Doctor user not found"));

        // Publish Event — NotificationEventListener handles in-app + email
        AppointmentBookedEvent event = AppointmentBookedEvent.builder()
                .appointmentId(savedAppointment.getId())
                .patientId(patient.getId())
                .doctorId(slot.getDoctor().getId())
                .startTime(slot.getStartTime())
                .endTime(slot.getEndTime())
                .doctorName("Dr. " + doctorUser.getFirstName() + " " + doctorUser.getLastName())
                .patientEmail(patientUser.getEmail())
                .build();
        eventPublisher.publishEvent(event);

        return savedAppointment;
    }

    @Transactional(readOnly = true)
    public List<com.healthcare.clinic.appointment.dto.AppointmentResponseDto> getPatientAppointments(Long userId) {
        return appointmentRepository.findAppointmentsForPatientWithNames(userId);
    }

    @Transactional(readOnly = true)
    public List<com.healthcare.clinic.appointment.dto.AppointmentResponseDto> getDoctorAppointments(Long userId) {
        return appointmentRepository.findAppointmentsForDoctorWithNames(userId);
    }

    @Transactional(readOnly = true)
    public List<com.healthcare.clinic.appointment.dto.AppointmentResponseDto> getTodayAppointments(Long doctorUserId) {
        ZonedDateTime startOfDay = ZonedDateTime.now().toLocalDate().atStartOfDay(java.time.ZoneId.systemDefault());
        ZonedDateTime endOfDay = startOfDay.plusDays(1).minusNanos(1);
        return appointmentRepository.findAppointmentsForDoctorToday(doctorUserId, startOfDay, endOfDay);
    }
    
    @Transactional(readOnly = true)
    public List<com.healthcare.clinic.appointment.dto.AppointmentResponseDto> getAllTodayAppointments() {
        ZonedDateTime startOfDay = ZonedDateTime.now().toLocalDate().atStartOfDay(java.time.ZoneId.systemDefault());
        ZonedDateTime endOfDay = startOfDay.plusDays(1).minusNanos(1);
        // We'll just return all today's appointments across the board for the queue. 
        // We'd ideally fetch by branch if multi-branch, but this is a good start.
        return appointmentRepository.findAllAppointmentsToday(startOfDay, endOfDay);
    }

    @Transactional(readOnly = true)
    public List<com.healthcare.clinic.appointment.dto.AppointmentResponseDto> getAppointmentsInRange(Long doctorUserId, ZonedDateTime start, ZonedDateTime end) {
        return appointmentRepository.findAppointmentsForDoctorToday(doctorUserId, start, end);
    }

    @Transactional
    public void updateAppointmentStatus(Long appointmentId, AppointmentStatus newStatus) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        
        AppointmentStatus currentStatus = appointment.getStatus();
        
        // Enforce valid transitions
        if (currentStatus == AppointmentStatus.CANCELLED || currentStatus == AppointmentStatus.COMPLETED || currentStatus == AppointmentStatus.NO_SHOW) {
            throw new IllegalArgumentException("Cannot change status from a terminal state: " + currentStatus);
        }
        
        if (newStatus == AppointmentStatus.BOOKED) {
            throw new IllegalArgumentException("Cannot transition back to BOOKED");
        }
        
        AppointmentStatus oldStatus = appointment.getStatus();
        appointment.setStatus(newStatus);
        appointmentRepository.save(appointment);
        
        eventPublisher.publishEvent(AppointmentStatusChangedEvent.builder()
                .appointmentId(appointmentId)
                .oldStatus(oldStatus)
                .newStatus(newStatus)
                .doctorId(appointment.getDoctor() != null ? appointment.getDoctor().getUserId() : null)
                .branchId(appointment.getBranchId())
                .build());

        if (newStatus == AppointmentStatus.COMPLETED && oldStatus != AppointmentStatus.COMPLETED) {
            eventPublisher.publishEvent(new AppointmentCompletedEvent(this, appointmentId));
        }
        
        if (newStatus == AppointmentStatus.CHECKED_IN) {
            generateTokenForAppointment(appointment);
        }
    }
    
    private void generateTokenForAppointment(Appointment appointment) {
        Long branchId = appointment.getBranchId();
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new RuntimeException("Branch not found"));
                
        ZonedDateTime startOfDay = ZonedDateTime.now().toLocalDate().atStartOfDay(ZonedDateTime.now().getZone());
        Integer maxToken = queueTokenRepository.findMaxTokenForBranchToday(branchId, startOfDay).orElse(0);
        
        QueueToken token = QueueToken.builder()
                .branch(branch)
                .appointment(appointment)
                .tokenNumber(maxToken + 1)
                .status("WAITING")
                .build();
                
        queueTokenRepository.save(token);
    }
    
    @Transactional
    public void cancelAppointment(Long appointmentId, String reason) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
                
        AppointmentStatus currentStatus = appointment.getStatus();
        if (currentStatus == AppointmentStatus.CANCELLED || currentStatus == AppointmentStatus.COMPLETED || currentStatus == AppointmentStatus.NO_SHOW) {
            throw new IllegalArgumentException("Cannot cancel an appointment that is already " + currentStatus);
        }
        
        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointment.setReasonForVisit(appointment.getReasonForVisit() + " (Cancelled: " + reason + ")");
        appointmentRepository.save(appointment);
        
        // Release the slot
        AppointmentSlot slot = appointment.getSlot();
        slot.setIsBooked(false);
        slotRepository.save(slot);
        
        User doctorUser = userRepository.findById(slot.getDoctor().getUserId())
                .orElseThrow(() -> new RuntimeException("Doctor user not found"));
                
        // Publish event
        AppointmentCancelledEvent event = AppointmentCancelledEvent.builder()
                .appointmentId(appointment.getId())
                .patientId(appointment.getPatient().getId())
                .doctorId(slot.getDoctor().getId())
                .startTime(slot.getStartTime())
                .doctorName("Dr. " + doctorUser.getFirstName() + " " + doctorUser.getLastName())
                .branchId(slot.getBranchId())
                .build();
        eventPublisher.publishEvent(event);
    }
    
    @Transactional
    public Appointment rescheduleAppointment(Long appointmentId, Long newSlotId) {
        // Cancel the old one
        cancelAppointment(appointmentId, "Rescheduled to a new slot");
        
        Appointment oldAppointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
                
        // Book the new one
        return bookAppointment(oldAppointment.getPatient().getUserId(), newSlotId, oldAppointment.getReasonForVisit());
    }
}
