package com.healthcare.clinic.doctor.controller;

import com.healthcare.clinic.doctor.service.TelemedicineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/telemedicine")
@RequiredArgsConstructor
public class TelemedicineController {

    private final TelemedicineService telemedicineService;

    @GetMapping("/room/{appointmentId}")
    @PreAuthorize("hasAnyRole('DOCTOR', 'PATIENT', 'ADMIN')")
    public ResponseEntity<Map<String, String>> getMeetingRoom(@PathVariable Long appointmentId) {
        String url = telemedicineService.generateMeetingUrl(appointmentId);
        Map<String, String> response = new HashMap<>();
        response.put("meetingUrl", url);
        return ResponseEntity.ok(response);
    }
}
