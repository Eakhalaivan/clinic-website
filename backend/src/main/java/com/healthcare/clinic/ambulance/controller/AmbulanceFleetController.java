package com.healthcare.clinic.ambulance.controller;

import com.healthcare.clinic.ambulance.entity.Ambulance;
import com.healthcare.clinic.ambulance.service.AmbulanceFleetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ambulance/fleet")
@RequiredArgsConstructor
public class AmbulanceFleetController {
    private final AmbulanceFleetService fleetService;

    @GetMapping("/ambulances")
    public ResponseEntity<List<Ambulance>> getAllAmbulances() {
        return ResponseEntity.ok(fleetService.getAllAmbulances());
    }
}
