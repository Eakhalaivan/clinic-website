package com.healthcare.clinic.patient.service;

import com.healthcare.clinic.appointment.dto.AppointmentResponseDto;
import com.healthcare.clinic.appointment.repository.AppointmentRepository;
import com.healthcare.clinic.doctor.dto.PrescriptionResponse;
import com.healthcare.clinic.doctor.service.PrescriptionService;
import com.healthcare.clinic.identity.entity.User;
import com.healthcare.clinic.identity.repository.UserRepository;
import com.healthcare.clinic.patient.dto.Patient360DTO;
import com.healthcare.clinic.patient.entity.PatientProfile;
import com.healthcare.clinic.patient.entity.Vitals;
import com.healthcare.clinic.patient.repository.PatientProfileRepository;
import com.healthcare.clinic.patient.repository.VitalsRepository;
import com.healthcare.clinic.laboratory.repository.LabTestRequestRepository;
import com.healthcare.clinic.laboratory.entity.LabTestRequest;
import com.healthcare.clinic.billing.repository.InvoiceRepository;
import com.healthcare.clinic.billing.entity.Invoice;
import com.healthcare.clinic.medicalrecord.repository.ClinicalNoteRepository;
import com.healthcare.clinic.medicalrecord.entity.ClinicalNote;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.time.ZonedDateTime;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class Patient360Service {

    private final PatientProfileRepository patientProfileRepository;
    private final UserRepository userRepository;
    private final VitalsRepository vitalsRepository;
    private final AppointmentRepository appointmentRepository;
    private final PrescriptionService prescriptionService;
    private final LabTestRequestRepository labTestRequestRepository;
    private final InvoiceRepository invoiceRepository;
    private final ClinicalNoteRepository clinicalNoteRepository;

    @Transactional(readOnly = true)
    public Patient360DTO getPatient360(Long userId) {
        PatientProfile profile = patientProfileRepository.findByUserId(userId)
                .orElse(null);
        
        User identity = userRepository.findById(userId).orElse(null);
        
        List<Vitals> recentVitals = vitalsRepository.findByPatientIdOrderByRecordedAtDesc(userId);
        
        List<AppointmentResponseDto> allAppointments = appointmentRepository.findAppointmentsForPatientWithNames(userId);
        
        ZonedDateTime now = ZonedDateTime.now();
        List<AppointmentResponseDto> upcomingAppointments = allAppointments.stream()
                .filter(a -> a.getStartTime() != null && a.getStartTime().isAfter(now))
                .collect(Collectors.toList());
        
        List<AppointmentResponseDto> recentAppointments = allAppointments.stream()
                .filter(a -> a.getStartTime() != null && a.getStartTime().isBefore(now))
                .limit(5)
                .collect(Collectors.toList());

        List<PrescriptionResponse> recentPrescriptions = prescriptionService.getPrescriptionsForPatient(userId)
                .stream()
                .limit(5)
                .collect(Collectors.toList());

        List<LabTestRequest> recentLabOrders = labTestRequestRepository.findByPatientIdOrderByRequestedAtDesc(userId)
                .stream()
                .limit(5)
                .collect(Collectors.toList());

        List<Invoice> invoices = invoiceRepository.findByPatientIdOrderByCreatedAtDesc(userId);
        
        List<ClinicalNote> clinicalNotes = clinicalNoteRepository.findByPatientIdOrderByCreatedAtDesc(userId);

        return Patient360DTO.builder()
                .profile(profile)
                .identity(identity)
                .recentVitals(recentVitals.size() > 5 ? recentVitals.subList(0, 5) : recentVitals)
                .recentAppointments(recentAppointments)
                .upcomingAppointments(upcomingAppointments)
                .recentPrescriptions(recentPrescriptions)
                .recentLabOrders(recentLabOrders)
                .invoices(invoices)
                .clinicalNotes(clinicalNotes)
                .build();
    }
}
