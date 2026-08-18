package com.healthcare.clinic.pharmacy.integration;

import com.healthcare.clinic.integration.PharmacyIntegrationClient;
import com.healthcare.clinic.integration.dto.PrescriptionIntegrationItemDTO;
import com.healthcare.clinic.pharmacy.entity.PharmacyPrescriptionItem;
import com.healthcare.clinic.pharmacy.service.PharmacyPrescriptionSyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PharmacyIntegrationClientImpl implements PharmacyIntegrationClient {

    private final PharmacyPrescriptionSyncService pharmacyPrescriptionSyncService;

    @Override
    public void syncNewPrescription(String patientName, String doctorName, Long clinicalPrescriptionId, List<PrescriptionIntegrationItemDTO> items) {
        List<PharmacyPrescriptionItem> pharmItems = mapItems(items);
        pharmacyPrescriptionSyncService.syncNewPrescription(patientName, doctorName, clinicalPrescriptionId, pharmItems);
    }

    @Override
    public void syncSendPrescription(String patientName, String doctorName, Long clinicalPrescriptionId, Long pharmacyUserId, List<PrescriptionIntegrationItemDTO> items) {
        List<PharmacyPrescriptionItem> pharmItems = mapItems(items);
        pharmacyPrescriptionSyncService.syncSendPrescription(patientName, doctorName, clinicalPrescriptionId, pharmacyUserId, pharmItems);
    }

    @Override
    public void syncVoidPrescription(Long clinicalPrescriptionId) {
        pharmacyPrescriptionSyncService.syncVoidPrescription(clinicalPrescriptionId);
    }

    @Override
    public java.util.Map<String, Object> getPharmacyPrescriptionStatus(Long clinicalPrescriptionId) {
        return pharmacyPrescriptionSyncService.getPharmacyPrescriptionStatus(clinicalPrescriptionId);
    }

    private List<PharmacyPrescriptionItem> mapItems(List<PrescriptionIntegrationItemDTO> items) {
        if (items == null) return List.of();
        return items.stream().map(item -> PharmacyPrescriptionItem.builder()
                .medicationName(item.getMedicationName())
                .type(item.getType())
                .dosage(item.getDosage())
                .frequency(item.getFrequency())
                .duration(item.getDuration())
                .instructions(item.getInstructions())
                .strength(item.getStrength())
                .timing(item.getTiming())
                .medicineId(item.getMedicineId())
                .prescribedQuantity(item.getPrescribedQuantity())
                .dispensedQuantity(item.getDispensedQuantity())
                .remainingQuantity(item.getRemainingQuantity())
                .build()).collect(Collectors.toList());
    }
}
