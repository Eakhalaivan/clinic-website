package com.healthcare.clinic.patient.service;

import com.healthcare.clinic.identity.entity.Role;
import com.healthcare.clinic.identity.entity.User;
import com.healthcare.clinic.identity.repository.RoleRepository;
import com.healthcare.clinic.identity.repository.UserRepository;
import com.healthcare.clinic.branch.entity.Branch;
import com.healthcare.clinic.branch.repository.BranchRepository;
import com.healthcare.clinic.patient.entity.PatientInsuranceClaim;
import com.healthcare.clinic.patient.entity.PatientNotification;
import com.healthcare.clinic.patient.entity.PatientPortalPayment;
import com.healthcare.clinic.patient.entity.PatientProfile;
import com.healthcare.clinic.patient.repository.PatientProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class PatientPortalBatch4IntegrationTest {

    @Autowired
    private PatientInsuranceClaimService insuranceClaimService;

    @Autowired
    private PatientPortalPaymentService patientPaymentService;

    @Autowired
    private PatientNotificationService notificationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PatientProfileRepository patientProfileRepository;

    @Autowired
    private BranchRepository branchRepository;

    private User testPatient;
    private Branch testBranch;

    @BeforeEach
    void setUp() {
        Role patientRole = roleRepository.findByName("ROLE_PATIENT")
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setName("ROLE_PATIENT");
                    return roleRepository.save(r);
                });

        testPatient = new User();
        testPatient.setEmail("testpatient_batch4_new@example.com");
        testPatient.setPasswordHash("password");
        testPatient.setFirstName("Test");
        testPatient.setLastName("Patient");
        testPatient.setRoles(Set.of(patientRole));
        testPatient = userRepository.save(testPatient);

        testBranch = new Branch();
        testBranch.setName("Main Branch");
        testBranch.setAddress("123 Branch St");
        testBranch.setCity("City");
        testBranch.setState("State");
        testBranch.setCountry("Country");
        testBranch.setPostalCode("12345");
        testBranch.setTimezone("UTC");
        testBranch = branchRepository.save(testBranch);

        PatientProfile profile = new PatientProfile();
        profile.setUserId(testPatient.getId());
        profile.setBranchId(testBranch.getId());
        profile.setDateOfBirth(LocalDate.of(1990, 1, 1));
        profile.setGender("Male");
        profile.setEmergencyContactName("Jane Doe");
        profile.setEmergencyContactPhone("0987654321");
        patientProfileRepository.save(profile);
    }

    @Test
    void testInsuranceClaimWorkflow() {
        PatientInsuranceClaim claim = new PatientInsuranceClaim();
        claim.setProvider("Blue Cross");
        claim.setPolicyNumber("POL-123");
        claim.setClaimAmount(new BigDecimal("150.00"));
        claim.setNotes("Test claim");

        PatientInsuranceClaim saved = insuranceClaimService.submitClaim(testPatient, claim);
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getStatus()).isEqualTo("Submitted");

        List<PatientInsuranceClaim> claims = insuranceClaimService.getClaims(testPatient);
        assertThat(claims).hasSize(1);
        assertThat(claims.get(0).getProvider()).isEqualTo("Blue Cross");
    }

    @Test
    void testPatientPaymentWorkflow() {
        PatientPortalPayment payment = new PatientPortalPayment();
        payment.setAmount(new BigDecimal("50.00"));
        payment.setPaymentMethod("Credit Card");

        PatientPortalPayment processed = patientPaymentService.processPayment(testPatient, payment);
        assertThat(processed.getId()).isNotNull();
        assertThat(processed.getStatus()).isEqualTo("Completed");
        assertThat(processed.getTransactionId()).startsWith("TXN-");

        List<PatientPortalPayment> payments = patientPaymentService.getPayments(testPatient);
        assertThat(payments).hasSize(1);
    }

    @Test
    void testNotificationWorkflow() {
        List<PatientNotification> initial = notificationService.getUnreadNotifications(testPatient);
        assertThat(initial).isEmpty();
    }
}
