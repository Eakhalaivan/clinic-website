package com.healthcare.clinic.ambulance.service;

import com.healthcare.clinic.ambulance.entity.EmergencyRequest;
import com.healthcare.clinic.ambulance.entity.AmbulanceAssignment;
import com.healthcare.clinic.ambulance.repository.*;
import com.healthcare.clinic.ambulance.entity.Ambulance;
import com.healthcare.clinic.doctor.entity.ClinicOutboxEvent;
import com.healthcare.clinic.doctor.repository.ClinicOutboxEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmergencyDispatchService {
    private final EmergencyRequestRepository requestRepository;
    private final AmbulanceAssignmentRepository assignmentRepository;
    private final AmbulanceRepository ambulanceRepository;
    private final ClinicOutboxEventRepository outboxEventRepository;

    @Transactional
    public EmergencyRequest intakeRequest(EmergencyRequest req) {
        return requestRepository.save(req);
    }
    
    @Transactional
    public AmbulanceAssignment assignNearestAmbulance(Long requestId) {
        EmergencyRequest req = requestRepository.findById(requestId)
            .orElseThrow(() -> new RuntimeException("Request not found"));
            
        if (!req.getStatus().equals("REQUESTED")) {
            throw new RuntimeException("Request is already processed");
        }
        
        List<Ambulance> available = ambulanceRepository.findNearestAvailable(
            req.getPickupLatitude(), req.getPickupLongitude());
            
        if (available.isEmpty()) {
            throw new RuntimeException("No available ambulances found");
        }
        
        Ambulance assigned = available.get(0);
        assigned.setStatus("DISPATCHED");
        ambulanceRepository.save(assigned);
        
        req.setStatus("DISPATCHED");
        req.setAssignedAmbulance(assigned);
        requestRepository.save(req);
        
        AmbulanceAssignment assignment = new AmbulanceAssignment();
        assignment.setRequest(req);
        assignment.setAmbulance(assigned);
        assignment.setStatus("ASSIGNED");
        
        AmbulanceAssignment savedAssignment = assignmentRepository.save(assignment);
        
        // Emit outbox notification
        ClinicOutboxEvent outboxEvent = ClinicOutboxEvent.builder()
            .aggregateType("AmbulanceAssignment")
            .aggregateId(savedAssignment.getId().toString())
            .eventType("AmbulanceAssigned")
            .payload("{\"requestId\": " + req.getId() + ", \"ambulanceId\": " + assigned.getId() + "}")
            .status("PENDING")
            .build();
        outboxEventRepository.save(outboxEvent);
        
        return savedAssignment;
    }
}
