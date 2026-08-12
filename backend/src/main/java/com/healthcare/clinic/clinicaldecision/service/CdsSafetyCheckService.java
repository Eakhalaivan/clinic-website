package com.healthcare.clinic.clinicaldecision.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthcare.clinic.clinicaldecision.entity.AlertStatus;
import com.healthcare.clinic.clinicaldecision.entity.CdsAlert;
import com.healthcare.clinic.clinicaldecision.entity.CdsRule;
import com.healthcare.clinic.clinicaldecision.entity.Severity;
import com.healthcare.clinic.clinicaldecision.entity.TriggerEvent;
import lombok.RequiredArgsConstructor;
import com.healthcare.clinic.clinicaldecision.exception.CdsCriticalSafetyException;
import com.healthcare.clinic.clinicaldecision.repository.CdsRuleRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import com.healthcare.clinic.patient.service.PatientAllergyService;
import com.healthcare.clinic.patient.service.PatientDiagnosisService;
import com.healthcare.clinic.patient.entity.PatientAllergy;
import com.healthcare.clinic.patient.entity.PatientDiagnosis;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CdsSafetyCheckService {

    private final CdsAlertService cdsAlertService;
    private final CdsRuleRepository cdsRuleRepository;
    private final ObjectMapper objectMapper;
    private final PatientAllergyService allergyService;
    private final PatientDiagnosisService diagnosisService;

    @Transactional
    public void performSynchronousSafetyCheck(Long patientId, List<String> medicationNames, Long doctorId) {
        if (medicationNames == null || medicationNames.isEmpty()) {
            return;
        }

        List<String> criticalAlerts = new ArrayList<>();
        List<String> warningAlerts = new ArrayList<>();


        
        List<PatientAllergy> activeAllergies = allergyService.getActiveAllergies(patientId);
        String patientAllergiesStr = activeAllergies.stream().map(a -> a.getAllergen().toUpperCase()).collect(Collectors.joining(","));
        
        List<PatientDiagnosis> activeDiagnoses = diagnosisService.getDiagnosesForPatient(patientId);
        String patientConditionsStr = activeDiagnoses.stream()
                .filter(d -> d.getClinicalStatus().equals("Active"))
                .map(d -> d.getDisplayName().toUpperCase())
                .collect(Collectors.joining(","));

        List<CdsRule> activeRules = new ArrayList<>();
        try {
            activeRules = cdsRuleRepository.findByTriggerEventAndIsActiveTrue(TriggerEvent.ON_PRESCRIPTION);
        } catch (Exception e) {
            log.warn("Could not fetch CDS rules, skipping rule evaluation", e);
        }

        for (String medName : medicationNames) {
            if (medName == null || medName.isBlank()) continue;
            String upperMed = medName.trim().toUpperCase();

            // Direct allergy string match
            if (patientAllergiesStr.contains(upperMed)) {
                String msg = "CRITICAL DRUG ALLERGY: Patient is allergic to '" + medName + "'.";
                criticalAlerts.add(msg);
                createAlert(patientId, doctorId, null, msg, Severity.CRITICAL);
            }

            for (CdsRule rule : activeRules) {
                try {
                    JsonNode conditions = objectMapper.readTree(rule.getConditions());
                    String type = conditions.has("type") ? conditions.get("type").asText() : "";
                    
                    if (conditions.has("medications")) {
                        boolean involvesMed = false;
                        for (JsonNode mNode : conditions.get("medications")) {
                            if (upperMed.contains(mNode.asText().toUpperCase())) {
                                involvesMed = true; break;
                            }
                        }
                        if (!involvesMed) continue;
                    }

                    if ("ALLERGY_CROSS_REACTIVITY".equals(type)) {
                        String allergy = conditions.has("allergy") ? conditions.get("allergy").asText().toUpperCase() : "";
                        if (!allergy.isEmpty() && patientAllergiesStr.contains(allergy)) {
                            String msg = "CRITICAL DRUG ALLERGY (" + rule.getName() + "): Patient has recorded allergy '" + allergy 
                                    + "' which is cross-reactive with prescribed medication '" + medName + "'.";
                            addAlert(msg, rule, criticalAlerts, warningAlerts, patientId, doctorId);
                        }
                    } else if ("DRUG_DISEASE_CONTRAINDICATION".equals(type)) {
                        if (conditions.has("conditions")) {
                            for (JsonNode cNode : conditions.get("conditions")) {
                                String condition = cNode.asText().toUpperCase();
                                if (patientConditionsStr.contains(condition)) {
                                    String msg = "CONTRAINDICATION (" + rule.getName() + "): '" + medName 
                                            + "' is contraindicated for patient with condition '" + condition + "'.";
                                    addAlert(msg, rule, criticalAlerts, warningAlerts, patientId, doctorId);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    log.error("Failed to parse conditions for rule " + rule.getId(), e);
                }
            }
        }

        // 3. Drug-Drug Interaction Check (Duplicate / Combo Checks)
        if (medicationNames.size() > 1) {
            Set<String> set = new HashSet<>();
            for (String m : medicationNames) {
                if (m != null && !set.add(m.trim().toUpperCase())) {
                    warningAlerts.add("WARNING DRUG-DRUG INTERACTION: Duplicate prescription item '" + m + "' detected in same order.");
                }
            }
        }

        if (!criticalAlerts.isEmpty()) {
            String combinedMessage = String.join(" | ", criticalAlerts);
            throw new CdsCriticalSafetyException(combinedMessage, criticalAlerts);
        }

        if (!warningAlerts.isEmpty()) {
            String combinedWarning = String.join(" | ", warningAlerts);
            CdsAlert alert = CdsAlert.builder()
                    .patientId(patientId)
                    .triggeredByUserId(doctorId)
                    .message(combinedWarning)
                    .severity(Severity.WARNING)
                    .status(AlertStatus.PENDING)
                    .build();
            cdsAlertService.saveAlertInNewTransaction(alert);
        }
    }

    private void addAlert(String msg, CdsRule rule, List<String> criticalAlerts, List<String> warningAlerts, Long patientId, Long doctorId) {
        if (rule.getSeverity() == Severity.CRITICAL) {
            criticalAlerts.add(msg);
        } else {
            warningAlerts.add(msg);
        }
        createAlert(patientId, doctorId, rule, msg, rule.getSeverity());
    }

    private void createAlert(Long patientId, Long doctorId, CdsRule rule, String message, Severity severity) {
        CdsAlert alert = CdsAlert.builder()
                .patientId(patientId)
                .triggeredByUserId(doctorId)
                .ruleId(rule != null ? rule.getId() : null)
                .message(message)
                .severity(severity)
                .status(AlertStatus.PENDING)
                .build();
        cdsAlertService.saveAlertInNewTransaction(alert);
    }
}
