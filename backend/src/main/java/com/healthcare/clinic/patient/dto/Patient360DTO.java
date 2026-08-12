package com.healthcare.clinic.patient.dto;

import com.healthcare.clinic.patient.entity.PatientProfile;
import com.healthcare.clinic.patient.entity.Vitals;
import com.healthcare.clinic.identity.entity.User;
import com.healthcare.clinic.appointment.dto.AppointmentResponseDto;
import com.healthcare.clinic.doctor.dto.PrescriptionResponse;
import com.healthcare.clinic.laboratory.entity.LabTestRequest;
import com.healthcare.clinic.billing.entity.Invoice;
import com.healthcare.clinic.medicalrecord.entity.ClinicalNote;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Patient360DTO {
    private PatientProfile profile;
    private User identity;
    private List<Vitals> recentVitals;
    private List<AppointmentResponseDto> recentAppointments;
    private List<AppointmentResponseDto> upcomingAppointments;
    private List<PrescriptionResponse> recentPrescriptions;
    private List<LabTestRequest> recentLabOrders;
    private List<Invoice> invoices;
    private List<ClinicalNote> clinicalNotes;
}
