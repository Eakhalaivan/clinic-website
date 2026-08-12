package com.healthcare.clinic.clinicaldecision.service;

import com.healthcare.clinic.clinicaldecision.entity.CdsAlert;
import com.healthcare.clinic.clinicaldecision.exception.CdsCriticalSafetyException;
import com.healthcare.clinic.patient.service.PatientAllergyService;
import com.healthcare.clinic.patient.service.PatientDiagnosisService;
import com.healthcare.clinic.patient.entity.PatientAllergy;
import com.healthcare.clinic.patient.entity.PatientDiagnosis;
import com.healthcare.clinic.clinicaldecision.repository.CdsRuleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CdsSafetyCheckServiceTest {

    @Mock
    private PatientAllergyService allergyService;

    @Mock
    private PatientDiagnosisService diagnosisService;

    @Mock
    private CdsAlertService cdsAlertService;

    @Mock
    private CdsRuleRepository cdsRuleRepository;

    @org.mockito.Spy
    private ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private CdsSafetyCheckService cdsSafetyCheckService;

    private PatientAllergy testAllergy;
    private PatientDiagnosis testDiagnosis;

    @BeforeEach
    void setUp() {
        testAllergy = new PatientAllergy();
        testAllergy.setAllergen("PENICILLIN");
        
        testDiagnosis = new PatientDiagnosis();
        testDiagnosis.setDisplayName("PREGNANCY");
        testDiagnosis.setClinicalStatus("Active");
    }

    @Test
    @DisplayName("Should pass safety check when prescription has no contraindicated drugs or allergies")
    void testSafetyCheck_CleanPrescription_Success() {
        when(allergyService.getActiveAllergies(100L)).thenReturn(List.of(testAllergy));
        when(diagnosisService.getDiagnosesForPatient(100L)).thenReturn(List.of(testDiagnosis));

        assertDoesNotThrow(() ->
                cdsSafetyCheckService.performSynchronousSafetyCheck(100L, List.of("Paracetamol", "Amlodipine"), 10L)
        );

        verify(cdsAlertService, never()).saveAlertInNewTransaction(any());
    }

    @Test
    @DisplayName("Should block and throw CdsCriticalSafetyException when drug allergy is detected")
    void testSafetyCheck_DrugAllergy_ThrowsExceptionAndSavesAlert() {
        when(allergyService.getActiveAllergies(100L)).thenReturn(List.of(testAllergy));
        when(diagnosisService.getDiagnosesForPatient(100L)).thenReturn(List.of(testDiagnosis));

        com.healthcare.clinic.clinicaldecision.entity.CdsRule rule1 = new com.healthcare.clinic.clinicaldecision.entity.CdsRule();
        rule1.setId(1L);
        rule1.setTriggerEvent(com.healthcare.clinic.clinicaldecision.entity.TriggerEvent.ON_PRESCRIPTION);
        rule1.setConditions("{\"type\": \"ALLERGY_CROSS_REACTIVITY\", \"medications\": [\"Amoxicillin\"], \"allergy\": \"PENICILLIN\"}");
        rule1.setSeverity(com.healthcare.clinic.clinicaldecision.entity.Severity.CRITICAL);
        when(cdsRuleRepository.findByTriggerEventAndIsActiveTrue(com.healthcare.clinic.clinicaldecision.entity.TriggerEvent.ON_PRESCRIPTION))
                .thenReturn(List.of(rule1));

        CdsCriticalSafetyException ex = assertThrows(CdsCriticalSafetyException.class, () ->
                cdsSafetyCheckService.performSynchronousSafetyCheck(100L, List.of("Amoxicillin 500mg"), 10L)
        );

        assertTrue(ex.getMessage().contains("CRITICAL DRUG ALLERGY"));
        verify(cdsAlertService, times(1)).saveAlertInNewTransaction(any(CdsAlert.class));
    }

    @Test
    @DisplayName("Should block and throw CdsCriticalSafetyException when drug-disease contraindication is detected")
    void testSafetyCheck_DrugDiseaseContraindication_ThrowsException() {
        when(allergyService.getActiveAllergies(100L)).thenReturn(List.of(testAllergy));
        when(diagnosisService.getDiagnosesForPatient(100L)).thenReturn(List.of(testDiagnosis));

        com.healthcare.clinic.clinicaldecision.entity.CdsRule rule2 = new com.healthcare.clinic.clinicaldecision.entity.CdsRule();
        rule2.setId(2L);
        rule2.setTriggerEvent(com.healthcare.clinic.clinicaldecision.entity.TriggerEvent.ON_PRESCRIPTION);
        rule2.setConditions("{\"type\": \"DRUG_DISEASE_CONTRAINDICATION\", \"medications\": [\"Ibuprofen\"], \"conditions\": [\"PREGNANCY\"]}");
        rule2.setSeverity(com.healthcare.clinic.clinicaldecision.entity.Severity.CRITICAL);
        when(cdsRuleRepository.findByTriggerEventAndIsActiveTrue(com.healthcare.clinic.clinicaldecision.entity.TriggerEvent.ON_PRESCRIPTION))
                .thenReturn(List.of(rule2));

        CdsCriticalSafetyException ex = assertThrows(CdsCriticalSafetyException.class, () ->
                cdsSafetyCheckService.performSynchronousSafetyCheck(100L, List.of("Ibuprofen 400mg"), 10L)
        );

        assertTrue(ex.getMessage().contains("CONTRAINDICATION"));
        verify(cdsAlertService, times(1)).saveAlertInNewTransaction(any(CdsAlert.class));
    }
}
