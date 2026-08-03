package com.healthcare.clinic.clinicaldecision.service;

import com.healthcare.clinic.clinicaldecision.entity.CdsAlert;
import com.healthcare.clinic.clinicaldecision.exception.CdsCriticalSafetyException;
import com.healthcare.clinic.patient.entity.PatientProfile;
import com.healthcare.clinic.patient.repository.PatientProfileRepository;
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
    private PatientProfileRepository patientProfileRepository;

    @Mock
    private CdsAlertService cdsAlertService;

    @InjectMocks
    private CdsSafetyCheckService cdsSafetyCheckService;

    private PatientProfile testPatient;

    @BeforeEach
    void setUp() {
        testPatient = PatientProfile.builder()
                .id(1L)
                .userId(100L)
                .allergies("[\"PENICILLIN\", \"SULFA\"]")
                .chronicConditions("[\"CKD\", \"PREGNANCY\"]")
                .branchId(1L)
                .build();
    }

    @Test
    @DisplayName("Should pass safety check when prescription has no contraindicated drugs or allergies")
    void testSafetyCheck_CleanPrescription_Success() {
        when(patientProfileRepository.findByUserId(100L)).thenReturn(Optional.of(testPatient));

        assertDoesNotThrow(() ->
                cdsSafetyCheckService.performSynchronousSafetyCheck(100L, List.of("Paracetamol", "Amlodipine"), 10L)
        );

        verify(cdsAlertService, never()).saveAlertInNewTransaction(any());
    }

    @Test
    @DisplayName("Should block and throw CdsCriticalSafetyException when drug allergy is detected")
    void testSafetyCheck_DrugAllergy_ThrowsExceptionAndSavesAlert() {
        when(patientProfileRepository.findByUserId(100L)).thenReturn(Optional.of(testPatient));

        CdsCriticalSafetyException ex = assertThrows(CdsCriticalSafetyException.class, () ->
                cdsSafetyCheckService.performSynchronousSafetyCheck(100L, List.of("Amoxicillin 500mg"), 10L)
        );

        assertTrue(ex.getMessage().contains("CRITICAL DRUG ALLERGY"));
        verify(cdsAlertService, times(1)).saveAlertInNewTransaction(any(CdsAlert.class));
    }

    @Test
    @DisplayName("Should block and throw CdsCriticalSafetyException when drug-disease contraindication is detected")
    void testSafetyCheck_DrugDiseaseContraindication_ThrowsException() {
        when(patientProfileRepository.findByUserId(100L)).thenReturn(Optional.of(testPatient));

        CdsCriticalSafetyException ex = assertThrows(CdsCriticalSafetyException.class, () ->
                cdsSafetyCheckService.performSynchronousSafetyCheck(100L, List.of("Ibuprofen 400mg"), 10L)
        );

        assertTrue(ex.getMessage().contains("CRITICAL CONTRAINDICATION"));
        verify(cdsAlertService, times(1)).saveAlertInNewTransaction(any(CdsAlert.class));
    }
}
