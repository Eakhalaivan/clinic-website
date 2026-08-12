package com.healthcare.clinic.integration;

import com.healthcare.clinic.integration.dto.PrescriptionIntegrationItemDTO;
import java.util.List;

public interface PharmacyIntegrationClient {
    void syncNewPrescription(String patientName, String doctorName, Long clinicalPrescriptionId, List<PrescriptionIntegrationItemDTO> items);
    void syncSendPrescription(String patientName, String doctorName, Long clinicalPrescriptionId, Long pharmacyUserId, List<PrescriptionIntegrationItemDTO> items);
    void syncVoidPrescription(Long clinicalPrescriptionId);
}
