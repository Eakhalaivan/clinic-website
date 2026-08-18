package com.healthcare.clinic.doctor.integration;

import com.healthcare.clinic.integration.ClinicIntegrationClient;
import com.healthcare.clinic.doctor.service.ClinicPrescriptionSyncService;
import com.healthcare.clinic.billing.service.BillingService;
import com.healthcare.clinic.billing.dto.InvoiceRequest;
import com.healthcare.clinic.billing.dto.InvoiceItemRequest;
import com.healthcare.clinic.billing.entity.ItemType;
import com.healthcare.clinic.doctor.repository.PrescriptionRepository;
import com.healthcare.clinic.doctor.entity.Prescription;
import com.healthcare.clinic.integration.dto.PharmacyInvoiceItemDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClinicIntegrationClientImpl implements ClinicIntegrationClient {

    private final ClinicPrescriptionSyncService clinicPrescriptionSyncService;
    private final BillingService billingService;
    private final PrescriptionRepository prescriptionRepository;

    @Override
    public void syncClinicalStatus(Long clinicalPrescriptionId, String status, String pharmacistUsername, LocalDateTime dispensedAt, java.util.List<java.util.Map<String, Object>> dispensedItems) {
        clinicPrescriptionSyncService.syncClinicalStatus(clinicalPrescriptionId, status, pharmacistUsername, dispensedAt, dispensedItems);
    }

    @Override
    public void createPharmacyInvoice(Long clinicalPrescriptionId, List<PharmacyInvoiceItemDTO> items, String description) {
        Prescription prescription = prescriptionRepository.findById(clinicalPrescriptionId)
                .orElseThrow(() -> new RuntimeException("Clinical Prescription not found"));

        List<InvoiceItemRequest> invoiceItems = items.stream().map(i -> 
                InvoiceItemRequest.builder()
                        .description(i.getDescription())
                        .quantity(i.getQuantity())
                        .unitPrice(i.getUnitPrice())
                        .itemType(ItemType.PHARMACY)
                        .referenceId(clinicalPrescriptionId)
                        .build()
        ).collect(Collectors.toList());

        InvoiceRequest invoiceRequest = InvoiceRequest.builder()
                .patientId(prescription.getPatientId())
                .appointmentId(prescription.getAppointmentId())
                .description(description)
                .items(invoiceItems)
                .dueDate(LocalDateTime.now().plusDays(7))
                .build();
                
        try {
            billingService.createInvoice(invoiceRequest);
        } catch (Exception e) {
            log.error("Failed to create pharmacy invoice", e);
            throw new RuntimeException("Failed to generate invoice", e);
        }
    }
}
