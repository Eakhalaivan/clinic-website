package com.healthcare.clinic.patient.service;

import com.healthcare.clinic.billing.entity.Invoice;
import com.healthcare.clinic.billing.repository.InvoiceRepository;
import com.healthcare.clinic.doctor.entity.Prescription;
import com.healthcare.clinic.doctor.repository.PrescriptionRepository;
import com.healthcare.clinic.medicalrecord.entity.ClinicalNote;
import com.healthcare.clinic.medicalrecord.repository.ClinicalNoteRepository;
import com.healthcare.clinic.patient.dto.TimelineEventDTO;
import com.healthcare.clinic.radiology.entity.RadiologyReport;
import com.healthcare.clinic.radiology.repository.RadiologyReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientTimelineService {

    private final RadiologyReportRepository radiologyReportRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final ClinicalNoteRepository clinicalNoteRepository;
    private final InvoiceRepository invoiceRepository;

    public List<TimelineEventDTO> getTimelineForPatient(Long patientId) {
        List<TimelineEventDTO> timeline = new ArrayList<>();

        // 1. Radiology Reports
        List<RadiologyReport> reports = radiologyReportRepository.findByRequestPatientId(patientId);
        for (RadiologyReport report : reports) {
            String title = report.getRequest() != null && report.getRequest().getProcedure() != null 
                    ? report.getRequest().getProcedure().getName() + " Scan"
                    : "Radiology Report";
                    
            timeline.add(TimelineEventDTO.builder()
                    .id("RAD-" + report.getId())
                    .type("RADIOLOGY")
                    .title(title)
                    .description(report.getImpression())
                    .status(report.getStatus())
                    .eventDate(report.getCreatedAt())
                    .referenceId(report.getId())
                    .build());
        }

        // 2. Prescriptions
        List<Prescription> prescriptions = prescriptionRepository.findByPatientIdOrderByCreatedAtDesc(patientId);
        for (Prescription rx : prescriptions) {
            timeline.add(TimelineEventDTO.builder()
                    .id("RX-" + rx.getId())
                    .type("PRESCRIPTION")
                    .title("Prescription by Doctor")
                    .description(rx.getDiagnosis() != null ? "Diagnosis: " + rx.getDiagnosis() : "Prescription issued")
                    .status(rx.getPharmacyStatus())
                    .eventDate(rx.getCreatedAt().atZone(ZoneId.systemDefault()))
                    .referenceId(rx.getId())
                    .build());
        }

        // 3. Clinical Notes
        List<ClinicalNote> notes = clinicalNoteRepository.findByPatientIdOrderByCreatedAtDesc(patientId);
        for (ClinicalNote note : notes) {
            timeline.add(TimelineEventDTO.builder()
                    .id("NOTE-" + note.getId())
                    .type("CLINICAL_NOTE")
                    .title("Clinical Consultation")
                    .description(note.getSubjective() != null ? note.getSubjective() : "Note recorded")
                    .status("RECORDED")
                    .eventDate(note.getCreatedAt().atZone(ZoneId.systemDefault()))
                    .referenceId(note.getId())
                    .build());
        }

        // 4. Invoices
        List<Invoice> invoices = invoiceRepository.findByPatientIdOrderByCreatedAtDesc(patientId);
        for (Invoice invoice : invoices) {
            timeline.add(TimelineEventDTO.builder()
                    .id("INV-" + invoice.getId())
                    .type("INVOICE")
                    .title("Billing Invoice")
                    .description("Amount: $" + invoice.getTotalAmount())
                    .status(invoice.getStatus().name())
                    .eventDate(invoice.getCreatedAt().atZone(ZoneId.systemDefault()))
                    .referenceId(invoice.getId())
                    .build());
        }

        // Sort chronologically descending (newest first)
        timeline.sort(Comparator.comparing(TimelineEventDTO::getEventDate).reversed());
        
        return timeline;
    }
}
