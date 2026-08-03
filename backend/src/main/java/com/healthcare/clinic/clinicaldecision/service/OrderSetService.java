package com.healthcare.clinic.clinicaldecision.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthcare.clinic.clinicaldecision.entity.OrderSetTemplate;
import com.healthcare.clinic.clinicaldecision.repository.OrderSetTemplateRepository;
import com.healthcare.clinic.doctor.dto.PrescriptionItemRequest;
import com.healthcare.clinic.doctor.dto.PrescriptionRequest;
import com.healthcare.clinic.doctor.service.PrescriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderSetService {

    private final OrderSetTemplateRepository orderSetTemplateRepository;
    private final PrescriptionService prescriptionService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Transactional(readOnly = true)
    public List<OrderSetTemplate> getOrderSetsForDiagnosis(String diagnosisCode) {
        if (diagnosisCode == null || diagnosisCode.isBlank()) {
            return orderSetTemplateRepository.findAll();
        }
        List<OrderSetTemplate> matches = orderSetTemplateRepository.findByDiagnosisCode(diagnosisCode);
        if (matches.isEmpty()) {
            return orderSetTemplateRepository.findByCategoryContainingIgnoreCase(diagnosisCode);
        }
        return matches;
    }

    @Transactional
    public Map<String, Object> applyOrderSet(Long templateId, Long patientId, Long doctorId) {
        OrderSetTemplate template = orderSetTemplateRepository.findById(templateId)
                .orElseThrow(() -> new RuntimeException("Order Set Template not found: " + templateId));

        List<PrescriptionItemRequest> medicationItems = new ArrayList<>();
        List<Map<String, Object>> skippedNonMedicationItems = new ArrayList<>();

        try {
            List<Map<String, Object>> rawItems = objectMapper.readValue(
                    template.getItems(), new TypeReference<List<Map<String, Object>>>() {});

            for (Map<String, Object> item : rawItems) {
                String type = (String) item.getOrDefault("type", "MEDICATION");
                if ("MEDICATION".equalsIgnoreCase(type)) {
                    PrescriptionItemRequest req = new PrescriptionItemRequest();
                    req.setMedicationName((String) item.getOrDefault("medicationName", item.getOrDefault("code", "Medication")));
                    req.setType((String) item.getOrDefault("medicationType", "Tablet"));
                    req.setDosage((String) item.getOrDefault("dosage", "1 tab"));
                    req.setFrequency((String) item.getOrDefault("frequency", "Daily"));
                    req.setDuration((String) item.getOrDefault("duration", "7 days"));
                    req.setInstructions((String) item.getOrDefault("instructions", "Take after food"));
                    medicationItems.add(req);
                } else {
                    // LAB / IMAGING / PROCEDURE items require clinician routing input
                    Map<String, Object> skipped = new HashMap<>(item);
                    skipped.put("reason", "Non-medication item type '" + type + "' requires manual catalog matching and routing by clinician.");
                    skippedNonMedicationItems.add(skipped);
                }
            }
        } catch (Exception e) {
            log.warn("Failed to parse OrderSet items JSON: {}", e.getMessage());
        }

        Object createdPrescription = null;
        if (!medicationItems.isEmpty()) {
            PrescriptionRequest pReq = new PrescriptionRequest();
            pReq.setPatientId(patientId);
            pReq.setNotes("Auto-generated from Smart Order Set: " + template.getName());
            pReq.setItems(medicationItems);

            // Routes through standard prescriptionService (executes CdsSafetyCheckService synchronous safety gate)
            createdPrescription = prescriptionService.createPrescription(pReq);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("orderSetId", template.getId());
        result.put("orderSetName", template.getName());
        result.put("patientId", patientId);
        result.put("appliedMedicationCount", medicationItems.size());
        result.put("createdPrescription", createdPrescription);
        result.put("skippedItems", skippedNonMedicationItems);

        return result;
    }
}
