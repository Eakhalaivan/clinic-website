package com.healthcare.clinic.integration;

import java.time.LocalDateTime;

public interface ClinicIntegrationClient {
    void syncClinicalStatus(Long clinicalPrescriptionId, String status, String pharmacistUsername, LocalDateTime dispensedAt, java.util.List<java.util.Map<String, Object>> dispensedItems);
    void createPharmacyInvoice(Long clinicalPrescriptionId, java.util.List<com.healthcare.clinic.integration.dto.PharmacyInvoiceItemDTO> items, String description);
}
