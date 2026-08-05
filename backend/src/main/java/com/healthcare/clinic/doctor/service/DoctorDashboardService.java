package com.healthcare.clinic.doctor.service;

import com.healthcare.clinic.appointment.entity.Appointment;
import com.healthcare.clinic.appointment.entity.AppointmentStatus;
import com.healthcare.clinic.appointment.repository.AppointmentRepository;
import com.healthcare.clinic.billing.entity.Invoice;
import com.healthcare.clinic.billing.entity.InvoiceStatus;
import com.healthcare.clinic.billing.repository.InvoiceRepository;
import com.healthcare.clinic.doctor.dto.DashboardStatsDTO;
import com.healthcare.clinic.doctor.entity.DoctorFollowUp;
import com.healthcare.clinic.doctor.entity.Prescription;
import com.healthcare.clinic.doctor.entity.DoctorProfile;
import com.healthcare.clinic.doctor.repository.DoctorFollowUpRepository;
import com.healthcare.clinic.doctor.repository.DoctorProfileRepository;
import com.healthcare.clinic.doctor.repository.PrescriptionRepository;
import com.healthcare.clinic.identity.entity.User;
import com.healthcare.clinic.identity.repository.UserRepository;
import com.healthcare.clinic.patient.entity.PatientProfile;
import com.healthcare.clinic.patient.repository.PatientProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorDashboardService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorFollowUpRepository followUpRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final InvoiceRepository invoiceRepository;
    private final DoctorProfileRepository doctorProfileRepository;
    private final UserRepository userRepository;
    private final PatientProfileRepository patientProfileRepository;

    @Transactional(readOnly = true)
    public DashboardStatsDTO getDashboardStats(Long userId) {
        DoctorProfile doctor = doctorProfileRepository.findByUserId(userId).orElse(null);
        Long doctorId = doctor != null ? doctor.getId() : -1L;

        List<Appointment> allAppointments = appointmentRepository.findByDoctor_UserId(userId);
        
        LocalDate today = LocalDate.now();
        
        // 1. Today's Appointments
        List<Appointment> todayAppointments = allAppointments.stream()
                .filter(a -> a.getSlot() != null && a.getSlot().getStartTime() != null && a.getSlot().getStartTime().toLocalDate().isEqual(today))
                .toList();
                
        int todayAppointmentsCount = todayAppointments.size();
        
        // 2. Queue Status
        int waitingCount = (int) todayAppointments.stream().filter(a -> AppointmentStatus.WAITING == a.getStatus() || AppointmentStatus.CHECKED_IN == a.getStatus()).count();
        int completedCount = (int) todayAppointments.stream().filter(a -> AppointmentStatus.COMPLETED == a.getStatus()).count();
        int emergencyCount = (int) todayAppointments.stream().filter(a -> AppointmentStatus.IN_CONSULTATION == a.getStatus()).count();

        // 3. Total Patients
        Set<Long> uniquePatients = allAppointments.stream()
                .filter(a -> a.getPatient() != null)
                .map(a -> a.getPatient().getId())
                .collect(Collectors.toSet());
        int totalPatients = uniquePatients.size();

        // 4. Follow-ups Today
        List<DoctorFollowUp> allFollowUps = followUpRepository.findByDoctorIdOrderByFollowUpDateAsc(doctorId);
        int followUpsToday = (int) allFollowUps.stream()
                .filter(f -> f.getFollowUpDate() != null && f.getFollowUpDate().isEqual(today))
                .count();

        // 5. Prescriptions Today
        List<Prescription> allPrescriptions = prescriptionRepository.findByDoctorIdOrderByCreatedAtDesc(doctorId);
        int prescriptionsToday = (int) allPrescriptions.stream()
                .filter(p -> p.getCreatedAt() != null && p.getCreatedAt().toLocalDate().isEqual(today))
                .count();

        // 6. Earnings Today (70% share)
        List<Long> appointmentIds = allAppointments.stream().map(Appointment::getId).collect(Collectors.toList());
        List<Invoice> allInvoices = appointmentIds.isEmpty() ? new ArrayList<>() : invoiceRepository.findByAppointmentIdIn(appointmentIds);
        
        BigDecimal todayEarnings = BigDecimal.ZERO;
        BigDecimal sharePercentage = new BigDecimal("0.70");
        for (Invoice invoice : allInvoices) {
            if (invoice.getStatus() == InvoiceStatus.PAID && invoice.getPaidAt() != null) {
                if (invoice.getPaidAt().toLocalDate().isEqual(today)) {
                    BigDecimal totalAmount = invoice.getTotalAmount() != null ? invoice.getTotalAmount() : BigDecimal.ZERO;
                    BigDecimal doctorShare = totalAmount.multiply(sharePercentage).setScale(2, RoundingMode.HALF_UP);
                    todayEarnings = todayEarnings.add(doctorShare);
                }
            }
        }

        // 7. Recent Activity
        final List<DashboardStatsDTO.Activity> activities = new ArrayList<>();
        
        // Add recent completed appointments
        allAppointments.stream()
                .filter(a -> AppointmentStatus.COMPLETED == a.getStatus() && a.getSlot() != null && a.getSlot().getEndTime() != null)
                .sorted((a1, a2) -> a2.getSlot().getEndTime().compareTo(a1.getSlot().getEndTime()))
                .limit(3)
                .forEach(a -> {
                    String patientName = getPatientNameFromProfile(a.getPatient());
                    activities.add(DashboardStatsDTO.Activity.builder()
                        .id(UUID.randomUUID().toString())
                        .type("CONSULTATION")
                        .description("Completed consultation with " + patientName)
                        .date(a.getSlot().getEndTime().toLocalDate().toString())
                        .time(a.getSlot().getEndTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")))
                        .build());
                });
                        
        // Add recent prescriptions
        allPrescriptions.stream()
                .filter(p -> p.getCreatedAt() != null)
                .limit(3)
                .forEach(p -> {
                    String patientName = getPatientNameFromId(p.getPatientId());
                    activities.add(DashboardStatsDTO.Activity.builder()
                        .id(UUID.randomUUID().toString())
                        .type("PRESCRIPTION")
                        .description("Sent prescription to " + patientName)
                        .date(p.getCreatedAt().toLocalDate().toString())
                        .time(p.getCreatedAt().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")))
                        .build());
                });

        // Sort activities by date/time descending and take top 5
        activities.sort((a, b) -> {
            try {
                String timeA = a.getTime() != null ? (a.getTime().length() == 4 ? "0" + a.getTime() : a.getTime()) : "00:00";
                String timeB = b.getTime() != null ? (b.getTime().length() == 4 ? "0" + b.getTime() : b.getTime()) : "00:00";
                String dateA = a.getDate() != null ? a.getDate() : LocalDate.now().toString();
                String dateB = b.getDate() != null ? b.getDate() : LocalDate.now().toString();
                
                LocalDateTime dt1 = LocalDateTime.parse(dateA + "T" + (timeA.length() > 5 ? timeA.substring(0, 5) : timeA));
                LocalDateTime dt2 = LocalDateTime.parse(dateB + "T" + (timeB.length() > 5 ? timeB.substring(0, 5) : timeB));
                return dt2.compareTo(dt1);
            } catch (Exception e) {
                return 0;
            }
        });
        
        List<DashboardStatsDTO.Activity> recentActivity = activities.size() > 5 ? activities.subList(0, 5) : activities;

        // 8. Weekly Patients Chart (Last 7 days)
        List<DashboardStatsDTO.ChartData> weeklyPatientsChart = new ArrayList<>();
        for (int i = 6; i >= 0; i--) {
            LocalDate d = today.minusDays(i);
            long count = allAppointments.stream()
                    .filter(a -> a.getSlot() != null && a.getSlot().getStartTime() != null && a.getSlot().getStartTime().toLocalDate().isEqual(d) && a.getPatient() != null)
                    .map(a -> a.getPatient().getId())
                    .distinct()
                    .count();
            weeklyPatientsChart.add(new DashboardStatsDTO.ChartData(d.format(DateTimeFormatter.ofPattern("EEE")), count));
        }

        // 9. Monthly Revenue Chart (Last 6 months)
        ZonedDateTime now = ZonedDateTime.now();
        List<DashboardStatsDTO.ChartData> monthlyRevenueChart = new ArrayList<>();
        
        for (int i = 5; i >= 0; i--) {
            Month m = now.minusMonths(i).getMonth();
            int year = now.minusMonths(i).getYear();
            String monthName = m.getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
            
            BigDecimal monthlyTotal = BigDecimal.ZERO;
            for (Invoice inv : allInvoices) {
                if (inv.getStatus() == InvoiceStatus.PAID && inv.getPaidAt() != null) {
                    if (inv.getPaidAt().getMonth() == m && inv.getPaidAt().getYear() == year) {
                        BigDecimal amount = inv.getTotalAmount() != null ? inv.getTotalAmount() : BigDecimal.ZERO;
                        monthlyTotal = monthlyTotal.add(amount.multiply(sharePercentage).setScale(2, RoundingMode.HALF_UP));
                    }
                }
            }
            monthlyRevenueChart.add(new DashboardStatsDTO.ChartData(monthName, monthlyTotal));
        }

        return DashboardStatsDTO.builder()
                .todayAppointments(todayAppointmentsCount)
                .totalPatients(totalPatients)
                .prescriptionsToday(prescriptionsToday)
                .followUpsToday(followUpsToday)
                .todayEarnings(todayEarnings)
                .waitingCount(waitingCount)
                .emergencyCount(emergencyCount)
                .completedCount(completedCount)
                .recentActivity(recentActivity)
                .weeklyPatientsChart(weeklyPatientsChart)
                .monthlyRevenueChart(monthlyRevenueChart)
                .build();
    }
    
    private String getPatientNameFromProfile(PatientProfile profile) {
        if (profile == null || profile.getUserId() == null) return "Unknown Patient";
        return userRepository.findById(profile.getUserId())
                .map(u -> u.getFirstName() + " " + u.getLastName())
                .orElse("Unknown Patient");
    }
    
    private String getPatientNameFromId(Long patientId) {
        if (patientId == null) return "Unknown Patient";
        return patientProfileRepository.findById(patientId)
                .flatMap(p -> userRepository.findById(p.getUserId()))
                .map(u -> u.getFirstName() + " " + u.getLastName())
                .orElse("Unknown Patient");
    }
}
