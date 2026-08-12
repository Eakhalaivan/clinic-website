package com.healthcare.clinic.laboratory.service;

import com.healthcare.clinic.identity.entity.Role;
import com.healthcare.clinic.identity.entity.User;
import com.healthcare.clinic.identity.repository.UserRepository;
import com.healthcare.clinic.laboratory.entity.LabResult;
import com.healthcare.clinic.laboratory.entity.LabTestCatalog;
import com.healthcare.clinic.laboratory.entity.LabTestRequest;
import com.healthcare.clinic.laboratory.repository.LabResultRepository;
import com.healthcare.clinic.laboratory.repository.LabTestCatalogRepository;
import com.healthcare.clinic.laboratory.repository.LabTestRequestRepository;
import com.healthcare.clinic.patient.entity.PatientProfile;
import com.healthcare.clinic.patient.repository.PatientProfileRepository;
import com.healthcare.clinic.branch.entity.Branch;
import com.healthcare.clinic.branch.repository.BranchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class LabBatch3IntegrationTest {

    @Autowired
    private LabReportVerificationService verificationService;

    @Autowired
    private LabReportPdfGenerator pdfGenerator;

    @Autowired
    private LabResultService resultService;

    @Autowired
    private LabTestRequestRepository requestRepository;

    @Autowired
    private LabResultRepository resultRepository;

    @Autowired
    private LabTestCatalogRepository catalogRepository;

    @Autowired
    private PatientProfileRepository patientProfileRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BranchRepository branchRepository;

    private User pathologist;
    private User labTech;
    private LabTestRequest pendingRequest;

    @BeforeEach
    public void setup() {
        resultRepository.deleteAll();
        requestRepository.deleteAll();
        catalogRepository.deleteAll();
        patientProfileRepository.deleteAll();
        userRepository.deleteAll();
        branchRepository.deleteAll();

        // Create Pathologist
        pathologist = new User();
        pathologist.setFirstName("John");
        pathologist.setLastName("Pathologist");
        pathologist.setEmail("patho@clinic.com");
        pathologist.setPasswordHash("test");
        userRepository.save(pathologist);

        // Create Tech
        labTech = new User();
        labTech.setFirstName("Tech");
        labTech.setLastName("One");
        labTech.setEmail("tech1@clinic.com");
        labTech.setPasswordHash("test");
        userRepository.save(labTech);

        // Create Patient User
        User patientUser = new User();
        patientUser.setFirstName("Pat");
        patientUser.setLastName("Ient");
        patientUser.setEmail("patient@clinic.com");
        patientUser.setPasswordHash("test");
        userRepository.save(patientUser);

        // Branch
        Branch branch = new Branch();
        branch.setName("Main");
        branch.setAddress("123 Main St");
        branch.setCity("City");
        branch.setState("State");
        branch.setCountry("Country");
        branch.setPostalCode("12345");
        branch.setTimezone("UTC");
        branch.setPhoneNumber("1234567890");
        branch.setEmail("main@clinic.com");
        branch = branchRepository.save(branch);

        // Patient Profile
        PatientProfile patient = new PatientProfile();
        patient.setUserId(patientUser.getId());
        patient.setBranchId(branch.getId());
        patient = patientProfileRepository.save(patient);

        // Catalog
        LabTestCatalog catalog = new LabTestCatalog();
        catalog.setTestCode("HGB");
        catalog.setTestName("Hemoglobin");
        catalog.setDepartment("Hematology");
        catalog.setReferenceRange("13.5 - 17.5");
        catalog.setPrice(new BigDecimal("15.00"));
        catalog.setBranch(branch);
        catalogRepository.save(catalog);

        // Request
        pendingRequest = new LabTestRequest();
        pendingRequest.setPatient(patient);
        pendingRequest.setTestCatalog(catalog);
        pendingRequest.setStatus("IN_PROGRESS");
        requestRepository.save(pendingRequest);

        // Add initial result
        LabResult res = new LabResult();
        res.setResultValue("14.2");
        resultService.addResult(pendingRequest.getId(), res, labTech);
        
        // Refresh request
        pendingRequest = requestRepository.findById(pendingRequest.getId()).get();
    }

    @Test
    public void testVerificationAndDigitalSignature() {
        // Verify result
        String comments = "Looks good. Normal levels.";
        LabResult verifiedResult = verificationService.verifyReport(pendingRequest.getId(), pathologist, comments);

        // Assertions
        assertThat(verifiedResult.getVerifiedBy().getId()).isEqualTo(pathologist.getId());
        assertThat(verifiedResult.getPathologistComments()).isEqualTo(comments);
        assertNotNull(verifiedResult.getVerifiedAt());

        LabTestRequest updatedRequest = requestRepository.findById(pendingRequest.getId()).get();
        assertThat(updatedRequest.getStatus()).isEqualTo("VERIFIED");
        assertNotNull(updatedRequest.getReleasedAt());
    }

    @Test
    public void testPdfGeneration() {
        verificationService.verifyReport(pendingRequest.getId(), pathologist, "Approved.");
        
        LabTestRequest updatedRequest = requestRepository.findById(pendingRequest.getId()).get();
        LabResult result = resultRepository.findByRequestId(updatedRequest.getId()).get();

        byte[] pdfBytes = pdfGenerator.generateLabReport(updatedRequest, result);

        assertNotNull(pdfBytes);
        assertThat(pdfBytes.length).isGreaterThan(0);
        
        // A minimal PDF byte array starts with %PDF
        String pdfHeader = new String(pdfBytes, 0, 4);
        assertThat(pdfHeader).isEqualTo("%PDF");
    }

    @Test
    public void testVerificationFailsIfNotPending() {
        // Change status to requested
        pendingRequest.setStatus("REQUESTED");
        requestRepository.save(pendingRequest);

        assertThrows(IllegalStateException.class, () -> {
            verificationService.verifyReport(pendingRequest.getId(), pathologist, "Should fail");
        });
    }
}
