package com.healthcare.clinic.ambulance.controller;

import com.healthcare.clinic.ambulance.entity.EmergencyRequest;
import com.healthcare.clinic.ambulance.service.EmergencyDispatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ambulance/dispatch")
@RequiredArgsConstructor
public class EmergencyDispatchController {
    private final EmergencyDispatchService dispatchService;

    @PostMapping("/request")
    public ResponseEntity<EmergencyRequest> createRequest(@RequestBody EmergencyRequest req) {
        return ResponseEntity.ok(dispatchService.intakeRequest(req));
    }
}
