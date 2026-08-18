package com.healthcare.clinic.admin.service;

import com.healthcare.clinic.admin.dto.AdminDashboardMetricsDto;
import com.healthcare.clinic.identity.repository.UserRepository;
import com.healthcare.clinic.patient.repository.PatientProfileRepository;
import com.healthcare.clinic.doctor.repository.DoctorProfileRepository;
import com.healthcare.clinic.appointment.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
@Service
@RequiredArgsConstructor
@Slf4j
public class AdminDashboardService {

    private final UserRepository userRepository;
    private final PatientProfileRepository patientProfileRepository;
    private final DoctorProfileRepository doctorProfileRepository;
    private final AppointmentRepository appointmentRepository;

    @Transactional(readOnly = true)
    public AdminDashboardMetricsDto getDashboardMetrics() {
        ZonedDateTime startOfDay = LocalDate.now().atStartOfDay(ZoneId.systemDefault());
        ZonedDateTime endOfDay = LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault());

        long totalPatients = patientProfileRepository.count();
        long totalDoctors = doctorProfileRepository.countByIsActiveTrue();
        // Assuming total users minus patients/doctors gives staff roughly, or we can count specific roles
        long activeUsers = userRepository.countByEnabledTrue();
        long inactiveUsers = userRepository.countByEnabledFalse();
        
        long todaysAppointments = appointmentRepository.countBySlotStartTimeBetween(startOfDay, endOfDay);
        long pendingAppointments = appointmentRepository.countByStatus(com.healthcare.clinic.appointment.entity.AppointmentStatus.BOOKED);
        long completedConsultations = appointmentRepository.countByStatus(com.healthcare.clinic.appointment.entity.AppointmentStatus.COMPLETED);

        // TODO: integrate Pharmacy, Lab, and Billing repositories for accurate real-time data
        
        return AdminDashboardMetricsDto.builder()
                .totalPatients(totalPatients)
                .totalDoctors(totalDoctors)
                .totalStaff(activeUsers - totalPatients - totalDoctors) // approximate for now
                .activeUsers(activeUsers)
                .inactiveUsers(inactiveUsers)
                .todaysAppointments(todaysAppointments)
                .pendingAppointments(pendingAppointments)
                .completedConsultations(completedConsultations)
                .pendingLabRequests(0L) // implement with LabRequestRepository
                .pendingPharmacyPrescriptions(0L) // implement with PrescriptionRepository
                .lowStockMedicines(0L) // implement with InventoryRepository
                .expiringMedicines(0L)
                .todaysRevenue(BigDecimal.ZERO) // implement with PaymentRepository
                .outstandingPayments(BigDecimal.ZERO)
                .build();
    }
}
