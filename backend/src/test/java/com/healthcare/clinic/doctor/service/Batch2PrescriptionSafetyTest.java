package com.healthcare.clinic.doctor.service;

import com.healthcare.clinic.clinicaldecision.service.CdsSafetyCheckService;
import com.healthcare.clinic.doctor.dto.PrescriptionRequest;
import com.healthcare.clinic.doctor.dto.PrescriptionResponse;
import com.healthcare.clinic.doctor.entity.Prescription;
import com.healthcare.clinic.doctor.repository.PrescriptionRepository;
import com.healthcare.clinic.patient.entity.PatientAllergy;
import com.healthcare.clinic.patient.repository.PatientAllergyRepository;
import com.healthcare.clinic.security.SecurityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mockStatic;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class Batch2PrescriptionSafetyTest {

    @Autowired
    private PrescriptionService prescriptionService;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private PatientAllergyRepository allergyRepository;

    @Autowired
    private CdsSafetyCheckService safetyCheckService;

    @BeforeEach
    void setup() {
        // Clear old test data
        allergyRepository.deleteAll();
        prescriptionRepository.deleteAll();
    }

    @Test
    void testSafetyCheckThrowsExceptionWhenAllergyMatches() {
        Long patientId = 1001L;

        // Add a critical allergy to Penicillin
        PatientAllergy allergy = new PatientAllergy();
        allergy.setPatientId(patientId);
        allergy.setAllergen("Penicillin");
        allergy.setAllergyType("Drug");
        allergy.setSeverity("Critical");
        allergy.setRecordedBy(999L);
        allergyRepository.save(allergy);

        boolean threwException = false;
        try {
            prescriptionService.performSafetyCheckOnly(patientId, List.of("Penicillin")); // Direct match
        } catch (com.healthcare.clinic.clinicaldecision.exception.CdsCriticalSafetyException e) {
            threwException = true;
            assertThat(e.getSafetyAlerts()).anyMatch(alert -> alert.contains("CRITICAL DRUG ALLERGY"));
        }

        assertThat(threwException).isTrue();
    }

    @Test
    void testDigitalSignPrescription() {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            Long doctorId = 999L;
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(doctorId);

            Prescription prescription = new Prescription();
            prescription.setDoctorId(doctorId);
            prescription.setPatientId(1001L);
            prescription.setStatus("Draft");
            prescription = prescriptionRepository.save(prescription);

            PrescriptionResponse response = prescriptionService.signPrescription(prescription.getId());

            assertThat(response.getStatus()).isEqualTo("Signed");
            assertThat(response.getSignedAt()).isNotNull();
            assertThat(response.getSignatureHash()).isNotNull();

            Prescription saved = prescriptionRepository.findById(prescription.getId()).get();
            assertThat(saved.getStatus()).isEqualTo("Signed");
            assertThat(saved.getSignatureHash()).isNotNull();
        }
    }
}
