package com.healthcare.clinic.nursing.service;

import com.healthcare.clinic.nursing.dto.*;
import com.healthcare.clinic.nursing.entity.*;
import com.healthcare.clinic.branch.entity.Branch;
import com.healthcare.clinic.branch.repository.BranchRepository;
import com.healthcare.clinic.identity.entity.Role;
import com.healthcare.clinic.identity.entity.User;
import com.healthcare.clinic.identity.repository.RoleRepository;
import com.healthcare.clinic.identity.repository.UserRepository;
import com.healthcare.clinic.patient.entity.PatientProfile;
import com.healthcare.clinic.patient.repository.PatientProfileRepository;
import com.healthcare.clinic.security.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class NursePortalIntegrationTest {

    @Autowired
    private NursingDocumentationService documentationService;
    
    @Autowired
    private NursingTaskService taskService;
    
    @Autowired
    private NursingEscalationService escalationService;

    @Autowired
    private PatientProfileRepository patientProfileRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BranchRepository branchRepository;

    private PatientProfile testPatient;
    private User testNurse;

    @BeforeEach
    void setUp() {
        Role nurseRole = roleRepository.findByName("ROLE_NURSE")
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setName("ROLE_NURSE");
                    return roleRepository.save(r);
                });

        testNurse = new User();
        testNurse.setEmail("nurse_test@example.com");
        testNurse.setPasswordHash("password");
        testNurse.setFirstName("Nurse");
        testNurse.setLastName("Test");
        testNurse.setRoles(Set.of(nurseRole));
        testNurse = userRepository.save(testNurse);

        Branch testBranch = new Branch();
        testBranch.setName("Main Branch");
        testBranch.setAddress("123 Branch St");
        testBranch.setCity("City");
        testBranch.setState("State");
        testBranch.setCountry("Country");
        testBranch.setPostalCode("12345");
        testBranch.setTimezone("UTC");
        testBranch = branchRepository.save(testBranch);

        testPatient = new PatientProfile();
        testPatient.setUserId(testNurse.getId()); // In reality, different user, but ID is enough
        testPatient.setBranchId(testBranch.getId());
        testPatient.setDateOfBirth(LocalDate.of(1990, 1, 1));
        testPatient.setGender("Female");
        testPatient = patientProfileRepository.save(testPatient);


        UserPrincipal principal = new UserPrincipal(
                testNurse.getId(),
                testNurse.getEmail(),
                List.of(new SimpleGrantedAuthority("ROLE_NURSE")),
                testBranch.getId()
        );

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(principal, "password", principal.getAuthorities())
        );
    }

    @Test
    void testNursingDocumentationWorkflow() {
        NursingNoteRequest noteReq = new NursingNoteRequest();
        noteReq.setPatientId(testPatient.getId());
        noteReq.setNoteType("PROGRESS");
        noteReq.setContent("Patient is resting comfortably.");
        
        NursingNote noteRes = documentationService.createNursingNote(noteReq);
        assertThat(noteRes).isNotNull();
        assertThat(noteRes.getNote()).isEqualTo("Patient is resting comfortably.");

        NursingCarePlanRequest planReq = new NursingCarePlanRequest();
        planReq.setPatientId(testPatient.getId());
        planReq.setDiagnosis("Risk of fall");
        planReq.setGoals("Patient will not fall");
        planReq.setInterventions("Assist with mobility");

        NursingCarePlan planRes = documentationService.createCarePlan(planReq);
        assertThat(planRes).isNotNull();
        assertThat(planRes.getStatus()).isEqualTo("ACTIVE");

        FallRiskAssessmentRequest fallReq = new FallRiskAssessmentRequest();
        fallReq.setPatientId(testPatient.getId());
        fallReq.setScore(45);
        fallReq.setNotes("High risk");

        FallRiskAssessment fallRes = documentationService.createFallRiskAssessment(fallReq);
        assertThat(fallRes.getRiskLevel()).isEqualTo("HIGH");

        PainAssessmentRequest painReq = new PainAssessmentRequest();
        painReq.setPatientId(testPatient.getId());
        painReq.setPainScore(8);
        painReq.setPainLocation("Back");
        painReq.setPainCharacteristics("Throbbing");
        painReq.setInterventions("Administered prescribed analgesic");

        PainAssessment painRes = documentationService.createPainAssessment(painReq);
        assertThat(painRes.getPainScore()).isEqualTo(8);

        List<NursingNote> notes = documentationService.getPatientNursingNotes(testPatient.getId());
        assertThat(notes).hasSize(1);
    }

    @Test
    void testNursingTaskWorkflow() {
        NursingTaskRequest taskReq = new NursingTaskRequest();
        taskReq.setPatientId(testPatient.getId());
        taskReq.setTaskType("OBSERVATION");
        taskReq.setDescription("Check vitals");
        taskReq.setDueTime(ZonedDateTime.now());
        
        NursingTask taskRes = taskService.createTask(taskReq);
        assertThat(taskRes.getStatus()).isEqualTo("PENDING");

        NursingTask updatedRes = taskService.updateTaskStatus(taskRes.getId(), "COMPLETED");
        assertThat(updatedRes.getStatus()).isEqualTo("COMPLETED");

        MedicationIncidentRequest incidentReq = new MedicationIncidentRequest();
        incidentReq.setPatientId(testPatient.getId());
        incidentReq.setMedicationName("Paracetamol");
        incidentReq.setIncidentType("WRONG_DOSE");
        incidentReq.setDescription("Given 1000mg instead of 500mg");
        incidentReq.setActionTaken("Doctor notified immediately");
        incidentReq.setDoctorNotified(true);
        incidentReq.setIncidentTime(ZonedDateTime.now());
        
        MedicationIncident incidentRes = taskService.reportMedicationIncident(incidentReq);
        assertThat(incidentRes.getIncidentType()).isEqualTo("WRONG_DOSE");
    }

    @Test
    void testEscalationAndChecklistWorkflow() {
        NursingChecklistRequest checklistReq = new NursingChecklistRequest();
        checklistReq.setPatientId(testPatient.getId());
        checklistReq.setChecklistType("ADMISSION");
        checklistReq.setItemsJson("[{\"id\":1, \"label\":\"Verify ID\", \"checked\":false}]");

        NursingChecklist checklistRes = escalationService.createChecklist(checklistReq);
        assertThat(checklistRes.getStatus()).isEqualTo("IN_PROGRESS");

        NurseEscalationRequest escReq = new NurseEscalationRequest();
        escReq.setPatientId(testPatient.getId());
        escReq.setPriority("URGENT");
        escReq.setReason("Sudden drop in blood pressure");
        escReq.setClinicalContext("Patient is post-op day 1");
        
        NurseEscalation escRes = escalationService.createEscalation(escReq);
        assertThat(escRes.getStatus()).isEqualTo("OPEN");
    }
}
