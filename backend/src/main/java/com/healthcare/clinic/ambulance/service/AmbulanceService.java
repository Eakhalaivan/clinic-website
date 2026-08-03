package com.healthcare.clinic.ambulance.service;

import com.healthcare.clinic.ambulance.entity.Ambulance;
import com.healthcare.clinic.ambulance.entity.EmergencyRequest;
import com.healthcare.clinic.ambulance.repository.AmbulanceRepository;
import com.healthcare.clinic.ambulance.repository.EmergencyRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AmbulanceService {

    private final AmbulanceRepository ambulanceRepo;
    private final EmergencyRequestRepository requestRepo;

    // ── Fleet Management ──────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<Ambulance> getAllFleet() {
        return ambulanceRepo.findByIsActiveTrue();
    }

    @Transactional
    public Ambulance addAmbulance(Ambulance ambulance) {
        return ambulanceRepo.save(ambulance);
    }

    @Transactional
    public Ambulance updateGpsLocation(Long ambulanceId, BigDecimal latitude, BigDecimal longitude) {
        Ambulance ambulance = ambulanceRepo.findById(ambulanceId).orElseThrow();
        ambulance.setCurrentLatitude(latitude);
        ambulance.setCurrentLongitude(longitude);
        return ambulanceRepo.save(ambulance);
    }

    @Transactional
    public Ambulance updateFleetStatus(Long ambulanceId, String status) {
        Ambulance ambulance = ambulanceRepo.findById(ambulanceId).orElseThrow();
        ambulance.setStatus(status);
        return ambulanceRepo.save(ambulance);
    }

    // ── Emergency Requests ────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<EmergencyRequest> getAllRequests() {
        return requestRepo.findAllByOrderByRequestedAtDesc();
    }

    @Transactional
    public EmergencyRequest createRequest(EmergencyRequest req) {
        String num = "EMR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        req.setRequestNumber(num);
        req.setStatus("REQUESTED");
        return requestRepo.save(req);
    }

    @Transactional
    public EmergencyRequest dispatchAmbulance(Long requestId, Long ambulanceId) {
        EmergencyRequest req = requestRepo.findById(requestId).orElseThrow();
        Ambulance ambulance = ambulanceRepo.findById(ambulanceId).orElseThrow();

        ambulance.setStatus("DISPATCHED");
        ambulanceRepo.save(ambulance);

        req.setAssignedAmbulance(ambulance);
        req.setStatus("DISPATCHED");
        return requestRepo.save(req);
    }

    @Transactional
    public EmergencyRequest updateRequestStatus(Long requestId, String status) {
        EmergencyRequest req = requestRepo.findById(requestId).orElseThrow();
        req.setStatus(status);
        if ("COMPLETED".equals(status) || "CANCELLED".equals(status)) {
            req.setCompletedAt(ZonedDateTime.now());
            // Free the ambulance
            if (req.getAssignedAmbulance() != null) {
                Ambulance ambulance = req.getAssignedAmbulance();
                ambulance.setStatus("AVAILABLE");
                ambulanceRepo.save(ambulance);
            }
        }
        return requestRepo.save(req);
    }
}
