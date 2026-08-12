package com.healthcare.clinic.ambulance.controller;

import com.healthcare.clinic.ambulance.entity.Ambulance;
import com.healthcare.clinic.ambulance.entity.EmergencyRequest;
import com.healthcare.clinic.ambulance.service.AmbulanceService;
import com.healthcare.clinic.ambulance.service.AmbulanceTrackingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/ambulance")
@RequiredArgsConstructor
@PreAuthorize("hasRole('AMBULANCE') or hasRole('SUPER_ADMIN') or hasRole('ADMIN')")
public class AmbulanceController {

    private final AmbulanceService ambulanceService;
    private final AmbulanceTrackingService trackingService;

    // SSE Tracking endpoint
    @GetMapping(value = "/tracking/live", produces = "text/event-stream")
    public SseEmitter streamLiveTracking() {
        return trackingService.subscribe();
    }

    // Fleet endpoints
    @GetMapping("/fleet")
    public ResponseEntity<List<Ambulance>> getFleet() {
        return ResponseEntity.ok(ambulanceService.getAllFleet());
    }

    @PostMapping("/fleet")
    public ResponseEntity<Ambulance> addAmbulance(@RequestBody Ambulance ambulance) {
        return ResponseEntity.ok(ambulanceService.addAmbulance(ambulance));
    }

    @PatchMapping("/fleet/{id}/location")
    public ResponseEntity<Ambulance> updateLocation(
            @PathVariable Long id,
            @RequestParam BigDecimal latitude,
            @RequestParam BigDecimal longitude) {
        return ResponseEntity.ok(ambulanceService.updateGpsLocation(id, latitude, longitude));
    }

    @PatchMapping("/fleet/{id}/status")
    public ResponseEntity<Ambulance> updateFleetStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        return ResponseEntity.ok(ambulanceService.updateFleetStatus(id, status));
    }

    // Emergency request endpoints
    @GetMapping("/requests")
    public ResponseEntity<List<EmergencyRequest>> getRequests() {
        return ResponseEntity.ok(ambulanceService.getAllRequests());
    }

    @PostMapping("/requests")
    public ResponseEntity<EmergencyRequest> createRequest(@RequestBody EmergencyRequest request) {
        return ResponseEntity.ok(ambulanceService.createRequest(request));
    }

    @PatchMapping("/requests/{id}/dispatch")
    public ResponseEntity<EmergencyRequest> dispatch(
            @PathVariable Long id,
            @RequestParam Long ambulanceId) {
        return ResponseEntity.ok(ambulanceService.dispatchAmbulance(id, ambulanceId));
    }

    @PatchMapping("/requests/{id}/status")
    public ResponseEntity<EmergencyRequest> updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        return ResponseEntity.ok(ambulanceService.updateRequestStatus(id, status));
    }
}
