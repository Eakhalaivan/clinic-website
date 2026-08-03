package com.healthcare.clinic.clinicaldecision.service;

import com.healthcare.clinic.clinicaldecision.entity.AlertStatus;
import com.healthcare.clinic.clinicaldecision.entity.CdsAlert;
import com.healthcare.clinic.clinicaldecision.entity.Severity;
import com.healthcare.clinic.clinicaldecision.exception.CdsCriticalSafetyException;
import com.healthcare.clinic.patient.entity.PatientProfile;
import com.healthcare.clinic.patient.repository.PatientProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Synchronous Blocking Clinical Decision Safety Check Service.
 * Called directly in-line prior to persisting a prescription entity.
 * Executes drug allergy, drug-disease contraindication, and drug-drug safety checks.
 *
 * NOTE ON MEDICATION MATCHING LIMITATION:
 * Prescriptions in this repository store medication names as free-text Strings (PrescriptionItem.medicationName)
 * rather than catalog foreign keys. This safety check performs a best-effort case-insensitive name match.
 *
 * NOTE ON CONTRAINDICATION DATA:
 * The drug-disease contraindication mappings below serve as demonstrative clinical rule examples.
 * They are not a substitute for a licensed commercial drug database (e.g., First Databank / Lexicomp).
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CdsSafetyCheckService {

    private final PatientProfileRepository patientProfileRepository;
    private final CdsAlertService cdsAlertService;

    // Demonstrative Drug-Disease Contraindications (Example Data)
    private static final Map<String, List<String>> CONTRAINDICATED_DISEASES = Map.of(
            "IBUPROFEN", List.of("CKD", "CHRONIC KIDNEY DISEASE", "PEPTIC ULCER", "RENAL FAILURE"),
            "NAPROXEN", List.of("CKD", "CHRONIC KIDNEY DISEASE", "PEPTIC ULCER"),
            "ASPIRIN", List.of("PEPTIC ULCER", "BLEEDING DISORDER"),
            "ENALAPRIL", List.of("PREGNANCY"),
            "LOSARTAN", List.of("PREGNANCY"),
            "METFORMIN", List.of("SEVERE RENAL IMPAIRMENT", "METABOLIC ACIDOSIS")
    );

    // Demonstrative Drug Allergy Cross-Reactivity (Example Data)
    private static final Map<String, List<String>> ALLERGY_CROSS_REACTIVITY = Map.of(
            "PENICILLIN", List.of("PENICILLIN", "AMOXICILLIN", "AMPICILLIN"),
            "SULFA", List.of("SULFA", "SULFAMETHOXAZOLE", "TRIMETHOPRIM"),
            "NSAID", List.of("IBUPROFEN", "NAPROXEN", "ASPIRIN")
    );

    @Transactional
    public void performSynchronousSafetyCheck(Long patientId, List<String> medicationNames, Long doctorId) {
        if (medicationNames == null || medicationNames.isEmpty()) {
            return;
        }

        List<String> criticalAlerts = new ArrayList<>();
        List<String> warningAlerts = new ArrayList<>();

        Optional<PatientProfile> patientOpt = patientProfileRepository.findByUserId(patientId);
        String patientAllergiesStr = patientOpt.map(p -> p.getAllergies() != null ? p.getAllergies().toUpperCase() : "").orElse("");
        String patientConditionsStr = patientOpt.map(p -> p.getChronicConditions() != null ? p.getChronicConditions().toUpperCase() : "").orElse("");

        for (String medName : medicationNames) {
            if (medName == null || medName.isBlank()) continue;
            String upperMed = medName.trim().toUpperCase();

            // 1. Drug Allergy Check
            for (Map.Entry<String, List<String>> entry : ALLERGY_CROSS_REACTIVITY.entrySet()) {
                String allergyKey = entry.getKey();
                List<String> relatedDrugs = entry.getValue();

                if (patientAllergiesStr.contains(allergyKey)) {
                    for (String drug : relatedDrugs) {
                        if (upperMed.contains(drug)) {
                            criticalAlerts.add("CRITICAL DRUG ALLERGY: Patient has recorded allergy '" + allergyKey
                                    + "' which is cross-reactive with prescribed medication '" + medName + "'.");
                        }
                    }
                }
            }

            // Direct allergy string match
            if (patientAllergiesStr.contains(upperMed)) {
                criticalAlerts.add("CRITICAL DRUG ALLERGY: Patient is allergic to '" + medName + "'.");
            }

            // 2. Drug-Disease Contraindication Check
            for (Map.Entry<String, List<String>> entry : CONTRAINDICATED_DISEASES.entrySet()) {
                String drugKey = entry.getKey();
                if (upperMed.contains(drugKey)) {
                    for (String disease : entry.getValue()) {
                        if (patientConditionsStr.contains(disease)) {
                            criticalAlerts.add("CRITICAL CONTRAINDICATION: '" + medName + "' is contraindicated for patient with condition '" + disease + "'.");
                        }
                    }
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

        // Handle CRITICAL findings: Persist audit alert in REQUIRES_NEW transaction and throw exception to rollback caller
        if (!criticalAlerts.isEmpty()) {
            String combinedMessage = String.join(" | ", criticalAlerts);
            
            CdsAlert alert = CdsAlert.builder()
                    .patientId(patientId)
                    .triggeredByUserId(doctorId)
                    .message(combinedMessage)
                    .severity(Severity.CRITICAL)
                    .status(AlertStatus.PENDING)
                    .build();

            // Saved in a separate transaction so the audit trail survives caller rollback
            cdsAlertService.saveAlertInNewTransaction(alert);

            throw new CdsCriticalSafetyException(combinedMessage, criticalAlerts);
        }

        // Handle WARNING findings: Persist warning alert for doctor without blocking save
        if (!warningAlerts.isEmpty()) {
            String combinedWarning = String.join(" | ", warningAlerts);
            CdsAlert warningAlert = CdsAlert.builder()
                    .patientId(patientId)
                    .triggeredByUserId(doctorId)
                    .message(combinedWarning)
                    .severity(Severity.WARNING)
                    .status(AlertStatus.PENDING)
                    .build();

            cdsAlertService.saveAlertInNewTransaction(warningAlert);
        }
    }
}
