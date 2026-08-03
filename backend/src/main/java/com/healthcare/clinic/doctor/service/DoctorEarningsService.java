package com.healthcare.clinic.doctor.service;

import com.healthcare.clinic.appointment.entity.Appointment;
import com.healthcare.clinic.appointment.repository.AppointmentRepository;
import com.healthcare.clinic.billing.entity.Invoice;
import com.healthcare.clinic.billing.entity.InvoiceStatus;
import com.healthcare.clinic.billing.repository.InvoiceRepository;
import com.healthcare.clinic.doctor.dto.DoctorEarningsResponse;
import com.healthcare.clinic.identity.entity.User;
import com.healthcare.clinic.identity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorEarningsService {

    private final AppointmentRepository appointmentRepository;
    private final InvoiceRepository invoiceRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public DoctorEarningsResponse getEarningsForDoctor(Long doctorId) {
        List<Appointment> doctorAppointments = appointmentRepository.findByDoctor_UserId(doctorId);
        List<Long> appointmentIds = doctorAppointments.stream().map(Appointment::getId).collect(Collectors.toList());
        
        List<Invoice> allInvoices = new ArrayList<>();
        if (!appointmentIds.isEmpty()) {
            allInvoices = invoiceRepository.findByAppointmentIdIn(appointmentIds);
        }

        BigDecimal todayEarnings = BigDecimal.ZERO;
        BigDecimal thisWeekEarnings = BigDecimal.ZERO;
        BigDecimal thisMonthEarnings = BigDecimal.ZERO;

        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1);
        LocalDate startOfMonth = today.withDayOfMonth(1);

        // Doctor share is 70% of the total amount
        BigDecimal sharePercentage = new BigDecimal("0.70");

        List<Invoice> paidInvoices = allInvoices.stream()
                .filter(i -> i.getStatus() == InvoiceStatus.PAID && i.getPaidAt() != null)
                .sorted(Comparator.comparing(Invoice::getPaidAt).reversed())
                .collect(Collectors.toList());

        for (Invoice invoice : paidInvoices) {
            LocalDate paidDate = invoice.getPaidAt().toLocalDate();
            BigDecimal doctorShare = invoice.getTotalAmount().multiply(sharePercentage).setScale(2, RoundingMode.HALF_UP);

            if (paidDate.isEqual(today)) {
                todayEarnings = todayEarnings.add(doctorShare);
            }
            if (!paidDate.isBefore(startOfWeek)) {
                thisWeekEarnings = thisWeekEarnings.add(doctorShare);
            }
            if (!paidDate.isBefore(startOfMonth)) {
                thisMonthEarnings = thisMonthEarnings.add(doctorShare);
            }
        }

        List<DoctorEarningsResponse.Payout> recentPayouts = paidInvoices.stream()
                .limit(10)
                .map(invoice -> {
                    User patient = userRepository.findById(invoice.getPatientId()).orElse(null);
                    String patientName = patient != null ? patient.getFirstName() + " " + patient.getLastName() : "Unknown";
                    BigDecimal doctorShare = invoice.getTotalAmount().multiply(sharePercentage).setScale(2, RoundingMode.HALF_UP);
                    return DoctorEarningsResponse.Payout.builder()
                            .date(invoice.getPaidAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                            .patient(patientName)
                            .type(invoice.getDescription() != null && !invoice.getDescription().isEmpty() ? invoice.getDescription() : "Consultation")
                            .fee(invoice.getTotalAmount())
                            .doctorShare(doctorShare)
                            .build();
                })
                .collect(Collectors.toList());

        return DoctorEarningsResponse.builder()
                .today(todayEarnings)
                .thisWeek(thisWeekEarnings)
                .thisMonth(thisMonthEarnings)
                .totalConsultations(doctorAppointments.size())
                .recentPayouts(recentPayouts)
                .build();
    }
}
