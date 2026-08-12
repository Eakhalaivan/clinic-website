package com.healthcare.clinic.radiology.service;

import com.healthcare.clinic.identity.entity.User;
import com.healthcare.clinic.identity.repository.UserRepository;
import com.healthcare.clinic.radiology.entity.ImagingProcedure;
import com.healthcare.clinic.radiology.entity.ImagingRequest;
import com.healthcare.clinic.radiology.repository.ImagingProcedureRepository;
import com.healthcare.clinic.radiology.repository.ImagingRequestRepository;
import com.healthcare.clinic.patient.entity.PatientProfile;
import com.healthcare.clinic.patient.repository.PatientProfileRepository;
import com.healthcare.clinic.doctor.entity.DoctorProfile;
import com.healthcare.clinic.doctor.repository.DoctorProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class RadiologyWorkflowIntegrationTest {

    @Autowired
    private RadiologyService radiologyService;

    @Autowired
    private ImagingRequestRepository requestRepository;
    
    @Autowired
    private ImagingProcedureRepository procedureRepository;
    
    @Autowired
    private PatientProfileRepository patientRepository;

    @Autowired
    private DoctorProfileRepository doctorRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private com.healthcare.clinic.identity.repository.RoleRepository roleRepository;

    private PatientProfile testPatient;
    private DoctorProfile testDoctor;
    private ImagingProcedure testProcedure;

    @BeforeEach
    void setUp() {
        User pUser = new User();
        pUser.setEmail("testp" + System.currentTimeMillis() + "@test.com");
        pUser.setPasswordHash("pass");
        pUser.setFirstName("Test");
        pUser.setLastName("Patient");
        pUser.setPhoneNumber("+12345678901");
        pUser = userRepository.save(pUser);
        
        testPatient = new PatientProfile();
        testPatient.setUserId(pUser.getId());
        testPatient.setEmergencyContactName("EContact");
        testPatient.setEmergencyContactPhone("+10987654321");
        testPatient.setBranchId(1L);
        testPatient = patientRepository.save(testPatient);

        User dUser = new User();
        dUser.setEmail("testd" + System.currentTimeMillis() + "@test.com");
        dUser.setPasswordHash("pass");
        dUser.setFirstName("Test");
        dUser.setLastName("Doc");
        dUser.setPhoneNumber("+12345678902");
        dUser = userRepository.save(dUser);
        
        testDoctor = new DoctorProfile();
        testDoctor.setUserId(dUser.getId());
        testDoctor.setSpecialty("Radiology");
        testDoctor.setRegistrationNumber("LIC123");
        testDoctor.setQualifications("MBBS, MD (Radiodiagnosis)");
        testDoctor.setBranchId(1L);
        testDoctor.setConsultationFee(java.math.BigDecimal.valueOf(50));
        testDoctor = doctorRepository.save(testDoctor);
        
        testProcedure = new ImagingProcedure();
        testProcedure.setName("Chest X-Ray");
        testProcedure.setCode("CXR01");
        testProcedure.setModality("CR");
        testProcedure.setPrice(java.math.BigDecimal.valueOf(100.0));
        testProcedure = procedureRepository.save(testProcedure);
    }

    @Test
    void testCompleteRadiologyWorkflow() {
        // 1. Order phase
        ImagingRequest newRequest = new ImagingRequest();
        newRequest.setPatient(testPatient);
        newRequest.setDoctor(testDoctor);
        newRequest.setProcedure(testProcedure);
        newRequest.setPriority("ROUTINE");
        newRequest.setClinicalNotes("Cough for 3 weeks");

        ImagingRequest created = radiologyService.createRequest(newRequest);
        assertThat(created.getId()).isNotNull();
        assertThat(created.getStatus()).isEqualTo("ORDERED");
        assertThat(created.getInvoice()).isNotNull(); // Billing integration triggered

        // 2. Schedule phase
        User patientUser = userRepository.findById(testPatient.getUserId()).orElseThrow();
        ImagingRequest scheduled = radiologyService.bookPatientRequest(created.getId(), ZonedDateTime.now().plusDays(1), patientUser);
        assertThat(scheduled.getStatus()).isEqualTo("SCHEDULED");
        
        // 3. Acquire Image phase
        ImagingRequest acquired = radiologyService.updateRequestStatus(scheduled.getId(), "IMAGE_ACQUIRED");
        assertThat(acquired.getStatus()).isEqualTo("IMAGE_ACQUIRED");
        
        // 4. Reporting phase
        ImagingRequest reporting = radiologyService.updateRequestStatus(acquired.getId(), "REPORTING");
        assertThat(reporting.getStatus()).isEqualTo("REPORTING");
        
        // 5. Verification phase
        ImagingRequest verified = radiologyService.updateRequestStatus(reporting.getId(), "VERIFIED");
        assertThat(verified.getStatus()).isEqualTo("VERIFIED");
        
        // 6. Release phase
        ImagingRequest released = radiologyService.updateRequestStatus(verified.getId(), "RELEASED");
        assertThat(released.getStatus()).isEqualTo("RELEASED");
    }

    @Test
    void testInvalidStateTransition_ThrowsException() {
        ImagingRequest newRequest = new ImagingRequest();
        newRequest.setPatient(testPatient);
        newRequest.setDoctor(testDoctor);
        newRequest.setProcedure(testProcedure);
        newRequest.setPriority("ROUTINE");

        ImagingRequest created = radiologyService.createRequest(newRequest);
        
        // Cannot go from ORDERED directly to VERIFIED
        assertThrows(IllegalStateException.class, () -> {
            radiologyService.updateRequestStatus(created.getId(), "VERIFIED");
        });
    }

    @Test
    void testDuplicateRequestPrevention_ThrowsException() {
        ImagingRequest request1 = new ImagingRequest();
        request1.setPatient(testPatient);
        request1.setDoctor(testDoctor);
        request1.setProcedure(testProcedure);
        request1.setPriority("ROUTINE");

        radiologyService.createRequest(request1);
        
        // Attempting the same request immediately
        ImagingRequest request2 = new ImagingRequest();
        request2.setPatient(testPatient);
        request2.setDoctor(testDoctor);
        request2.setProcedure(testProcedure);
        request2.setPriority("ROUTINE");
        
        assertThrows(IllegalArgumentException.class, () -> {
            radiologyService.createRequest(request2);
        });
    }
}
