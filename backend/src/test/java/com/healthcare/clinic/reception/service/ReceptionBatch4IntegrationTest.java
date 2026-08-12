package com.healthcare.clinic.reception.service;

import com.healthcare.clinic.branch.entity.Branch;
import com.healthcare.clinic.branch.repository.BranchRepository;
import com.healthcare.clinic.identity.entity.User;
import com.healthcare.clinic.identity.repository.UserRepository;
import com.healthcare.clinic.patient.entity.PatientDocument;
import com.healthcare.clinic.patient.entity.PatientProfile;
import com.healthcare.clinic.patient.repository.PatientProfileRepository;
import com.healthcare.clinic.reception.entity.KioskCheckin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.healthcare.clinic.ClinicApplication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = ClinicApplication.class)
@ActiveProfiles("test")
@Transactional
class ReceptionBatch4IntegrationTest {

    @Autowired
    private KioskService kioskService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private PatientProfileRepository patientProfileRepository;

    private Branch testBranch;
    private PatientProfile testProfile;
    private User testUser;

    @BeforeEach
    void setUp() {
        testBranch = new Branch();
        testBranch.setName("Test Branch B4");
        testBranch.setAddress("123 Test Street");
        testBranch.setCity("TestCity");
        testBranch.setState("TestState");
        testBranch.setCountry("India");
        testBranch.setPostalCode("000001");
        testBranch.setTimezone("Asia/Kolkata");
        testBranch = branchRepository.save(testBranch);

        testUser = new User();
        testUser.setEmail("kiosk.patient.b4@test.com");
        testUser.setFirstName("Kiosk");
        testUser.setLastName("Patient");
        testUser.setPasswordHash("hash");
        testUser = userRepository.save(testUser);

        testProfile = new PatientProfile();
        testProfile.setUserId(testUser.getId());
        testProfile.setBranchId(testBranch.getId());
        testProfile = patientProfileRepository.save(testProfile);
    }

    @Test
    void shouldAllowPatientToSelfCheckInViaKiosk() {
        KioskCheckin checkin = kioskService.selfCheckIn(
                testBranch.getId(), testProfile.getId(), null, "KIOSK-A");

        assertThat(checkin).isNotNull();
        assertThat(checkin.getId()).isNotNull();
        assertThat(checkin.getStatus()).isEqualTo("PENDING");
        assertThat(checkin.getCheckinMethod()).isEqualTo("KIOSK");
        assertThat(checkin.getKioskStation()).isEqualTo("KIOSK-A");
    }

    @Test
    void shouldVerifyKioskCheckin() {
        KioskCheckin checkin = kioskService.selfCheckIn(
                testBranch.getId(), testProfile.getId(), null, "KIOSK-B");

        KioskCheckin verified = kioskService.verifyCheckin(checkin.getId(), "CHECKED_IN");

        assertThat(verified.getStatus()).isEqualTo("CHECKED_IN");
        assertThat(verified.getVerifiedAt()).isNotNull();
    }

    @Test
    void shouldUploadDocumentForPatientFromReceptionDesk() {
        PatientDocument doc = kioskService.uploadDocumentForPatient(
                testProfile.getId(),
                testBranch.getId(),
                "X-Ray Report",
                "RADIOLOGY",
                null,
                "Fuji Scanner",
                "Uploaded at reception desk during check-in"
        );

        assertThat(doc).isNotNull();
        assertThat(doc.getId()).isNotNull();
        assertThat(doc.getTitle()).isEqualTo("X-Ray Report");
        assertThat(doc.getDocumentType()).isEqualTo("RADIOLOGY");

        List<PatientDocument> docs = kioskService.getPatientDocuments(testProfile.getId());
        assertThat(docs).isNotEmpty();
        assertThat(docs.get(0).getId()).isEqualTo(doc.getId());
    }

    @Test
    void shouldReturnDashboardStats() {
        kioskService.selfCheckIn(testBranch.getId(), testProfile.getId(), null, "KIOSK-1");
        kioskService.selfCheckIn(testBranch.getId(), testProfile.getId(), null, "KIOSK-2");

        Map<String, Object> stats = kioskService.getDashboardStats(testBranch.getId());

        assertThat(stats).isNotNull();
        assertThat(stats).containsKey("kioskPending");
        assertThat(stats).containsKey("queueWaiting");
        assertThat((long) stats.get("kioskPending")).isGreaterThanOrEqualTo(2L);
    }
}
