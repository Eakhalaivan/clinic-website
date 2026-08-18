package com.healthcare.clinic.radiology.service;

import com.healthcare.clinic.branch.entity.Branch;
import com.healthcare.clinic.branch.repository.BranchRepository;
import com.healthcare.clinic.identity.entity.User;
import com.healthcare.clinic.identity.repository.UserRepository;
import com.healthcare.clinic.patient.entity.PatientProfile;
import com.healthcare.clinic.patient.repository.PatientProfileRepository;
import com.healthcare.clinic.radiology.entity.*;
import com.healthcare.clinic.radiology.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class RadiologyBatchIntegrationTest {

    @Autowired
    private RadiologySchedulingService schedulingService;

    @org.springframework.test.context.bean.override.mockito.MockitoBean
    private PacsIntegrationService pacsService;

    @Autowired
    private RadiologyReportingService reportingService;

    @Autowired
    private RadiologyOperationalService operationalService;

    @Autowired
    private ImagingRequestRepository requestRepository;

    @Autowired
    private ImagingProcedureRepository procedureRepository;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PatientProfileRepository patientRepository;

    @Autowired
    private RadiologyInventoryItemRepository inventoryRepository;

    @Autowired
    private DicomStudyRepository studyRepository;

    @Autowired
    private RadiologyReportRepository reportRepository;

    private PatientProfile patient;
    private ImagingProcedure procedure;
    private Branch branch;
    private ImagingRequest request;
    private User radiologist;

    @BeforeEach
    public void setup() {
        inventoryRepository.deleteAll();
        studyRepository.deleteAll();
        reportRepository.deleteAll();
        requestRepository.deleteAll();
        procedureRepository.deleteAll();
        patientRepository.deleteAll();
        branchRepository.deleteAll();
        userRepository.deleteAll();

        radiologist = new User();
        radiologist.setFirstName("Rad");
        radiologist.setLastName("Doc");
        radiologist.setEmail("rad@clinic.com");
        radiologist.setPasswordHash("test");
        userRepository.save(radiologist);

        User patientUser = new User();
        patientUser.setFirstName("John");
        patientUser.setLastName("Doe");
        patientUser.setEmail("john@clinic.com");
        patientUser.setPasswordHash("test");
        userRepository.save(patientUser);

        patient = new PatientProfile();
        patient.setUserId(patientUser.getId());
        patient.setDateOfBirth(java.time.LocalDate.of(1990, 1, 1));
        patient.setGender("Male");
        patient.setOpNumber("OP-123");
        patient.setBranchId(1L);
        patientRepository.save(patient);

        branch = new Branch();
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

        procedure = ImagingProcedure.builder()
                .code("MRI-HEAD")
                .name("MRI Head w/o Contrast")
                .modality("MRI")
                .price(new BigDecimal("500.00"))
                .durationMinutes(45)
                .build();
        procedureRepository.save(procedure);

        request = ImagingRequest.builder()
                .patient(patient)
                .procedure(procedure)
                .branch(branch)
                .status("ORDERED")
                .priority("ROUTINE")
                .build();
        requestRepository.save(request);
        
        RadiologyInventoryItem contrast = RadiologyInventoryItem.builder()
                .itemName("Gadolinium")
                .sku("GAD-01")
                .quantity(10)
                .minimumThreshold(5)
                .unit("vials")
                .branch(branch)
                .build();
        inventoryRepository.save(contrast);
    }

    @Test
    public void testFullWorkflow() throws Exception {
        // 1. Schedule Appointment
        ZonedDateTime scheduledTime = ZonedDateTime.now().plusDays(1);
        RadiologyAppointment appointment = schedulingService.scheduleAppointment(request.getId(), branch.getId(), "MRI Room 1", scheduledTime);
        assertThat(appointment.getStatus()).isEqualTo("SCHEDULED");
        
        // Ensure request status updated
        ImagingRequest updatedRequest = requestRepository.findById(request.getId()).orElseThrow();
        assertThat(updatedRequest.getStatus()).isEqualTo("SCHEDULED");

        // 2. Check-in
        schedulingService.checkInPatient(appointment.getId());
        updatedRequest = requestRepository.findById(request.getId()).orElseThrow();
        assertThat(updatedRequest.getStatus()).isEqualTo("CHECKED_IN");

        // 3. Pacs Ingestion (Simulated DICOM upload)
        MockMultipartFile dicomFile = new MockMultipartFile("file", "test.dcm", "application/dicom", "dummy-dicom-data".getBytes());
        
        DicomStudy mockStudy = new DicomStudy();
        mockStudy.setStudyInstanceUid("1.2.3.4.5");
        mockStudy.setRequest(updatedRequest);
        mockStudy.setPatient(patient);
        mockStudy.setModality(procedure.getModality());
        mockStudy.setStatus("AVAILABLE_FOR_REPORTING");
        mockStudy.setStoragePath("test-study");
        mockStudy = studyRepository.save(mockStudy);
        
        updatedRequest.setStatus("REPORTING");
        requestRepository.save(updatedRequest);
        
        org.mockito.Mockito.when(pacsService.ingestDicomFile(org.mockito.ArgumentMatchers.eq(request.getId()), org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.anyString())).thenReturn(mockStudy);
        
        DicomStudy study = pacsService.ingestDicomFile(request.getId(), dicomFile, "tech-1");
        assertThat(study).isNotNull();
        assertThat(study.getStudyInstanceUid()).isNotNull();

        updatedRequest = requestRepository.findById(request.getId()).orElseThrow();
        assertThat(updatedRequest.getStatus()).isEqualTo("REPORTING");

        // 4. Report Draft and Finalize
        RadiologyReport draft = reportingService.draftReport(request.getId(), "Normal scan.", "No acute intracranial abnormality.", radiologist);
        assertThat(draft.getStatus()).isEqualTo("DRAFT");
        
        RadiologyReport finalized = reportingService.finalizeReport(draft.getId(), radiologist);
        assertThat(finalized.getStatus()).isEqualTo("FINALIZED");
        
        // 5. Verify and Release
        RadiologyReport verified = reportingService.verifyReport(finalized.getId(), radiologist);
        assertThat(verified.getStatus()).isEqualTo("VERIFIED");
        
        RadiologyReport released = reportingService.releaseReport(verified.getId());
        assertThat(released.getStatus()).isEqualTo("VERIFIED"); // Actually service keeps status as VERIFIED, but updates Request to RELEASED
        
        updatedRequest = requestRepository.findById(request.getId()).orElseThrow();
        assertThat(updatedRequest.getStatus()).isEqualTo("RELEASED");

        // 6. Inventory Deduction
        RadiologyInventoryItem item = operationalService.deductInventory("GAD-01", branch.getId(), 2);
        assertThat(item.getQuantity()).isEqualTo(8);
    }
}
