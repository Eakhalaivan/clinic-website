package com.healthcare.clinic.ambulance.integration;

import com.healthcare.clinic.ambulance.entity.Ambulance;
import com.healthcare.clinic.ambulance.entity.AmbulanceAssignment;
import com.healthcare.clinic.ambulance.entity.EmergencyPatientRecord;
import com.healthcare.clinic.ambulance.entity.EmergencyRequest;
import com.healthcare.clinic.ambulance.repository.AmbulanceAssignmentRepository;
import com.healthcare.clinic.ambulance.repository.AmbulanceRepository;
import com.healthcare.clinic.ambulance.repository.EmergencyRequestRepository;
import com.healthcare.clinic.ambulance.service.AmbulanceClinicalService;
import com.healthcare.clinic.ambulance.service.EmergencyDispatchService;
import com.healthcare.clinic.doctor.repository.ClinicOutboxEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class AmbulanceE2ETest {

    @Autowired
    private EmergencyDispatchService dispatchService;

    @Autowired
    private AmbulanceClinicalService clinicalService;

    @Autowired
    private AmbulanceRepository ambulanceRepository;

    @Autowired
    private EmergencyRequestRepository requestRepository;

    @Autowired
    private AmbulanceAssignmentRepository assignmentRepository;
    
    @Autowired
    private ClinicOutboxEventRepository outboxEventRepository;

    @BeforeEach
    void setUp() {
        ambulanceRepository.deleteAll();
        requestRepository.deleteAll();
        assignmentRepository.deleteAll();
        outboxEventRepository.deleteAll();

        Ambulance amb1 = new Ambulance();
        amb1.setVehicleNumber("TEST-123");
        amb1.setAmbulanceType("ALS");
        amb1.setStatus("AVAILABLE");
        amb1.setIsActive(true);
        amb1.setCurrentLatitude(new BigDecimal("40.7128"));
        amb1.setCurrentLongitude(new BigDecimal("-74.0060"));
        ambulanceRepository.save(amb1);
    }

    @Test
    @Transactional
    public void testFullDispatchToHandoffWorkflow() {
        // 1. Intake Request
        EmergencyRequest req = new EmergencyRequest();
        req.setRequestNumber("REQ-1001");
        req.setCallerName("John Doe");
        req.setCallerPhone("555-0100");
        req.setPickupAddress("123 Main St");
        req.setPickupLatitude(new BigDecimal("40.7130"));
        req.setPickupLongitude(new BigDecimal("-74.0050"));
        req.setStatus("REQUESTED");
        
        EmergencyRequest savedReq = dispatchService.intakeRequest(req);
        assertNotNull(savedReq.getId());
        assertEquals("REQUESTED", savedReq.getStatus());

        // 2. Assign Nearest Ambulance
        AmbulanceAssignment assignment = dispatchService.assignNearestAmbulance(savedReq.getId());
        assertNotNull(assignment.getId());
        assertEquals("ASSIGNED", assignment.getStatus());
        
        // Check Outbox Event for Assignment
        var outboxEvents = outboxEventRepository.findAll();
        assertTrue(outboxEvents.stream().anyMatch(e -> e.getEventType().equals("AmbulanceAssigned")));

        // 3. Clinical Handoff
        EmergencyPatientRecord record = new EmergencyPatientRecord();
        record.setRequest(savedReq);
        record.setVitalsSummary("Stable, BP: 120/80");
        record.setCrewNotes("Patient was responsive");
        
        EmergencyPatientRecord savedRecord = clinicalService.saveRecord(record);
        assertNotNull(savedRecord.getId());
        
        // Check Outbox Event for Handoff
        outboxEvents = outboxEventRepository.findAll();
        assertTrue(outboxEvents.stream().anyMatch(e -> e.getEventType().equals("AmbulanceHandoff")));
    }
}
