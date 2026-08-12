package com.healthcare.clinic.ai.controller;

import com.healthcare.clinic.ai.service.AiDoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai/doctor")
@RequiredArgsConstructor
public class AiDoctorController {
    
    private final AiDoctorService aiDoctorService;

    @PostMapping("/summarize-encounter")
    public ResponseEntity<String> summarizeEncounter(@RequestParam Long encounterId, @RequestParam Long doctorId, @RequestParam Long tenantId) {
        return ResponseEntity.ok(aiDoctorService.generateSummary(encounterId, doctorId, tenantId));
    }
    
    @PostMapping("/approve-summary")
    public ResponseEntity<?> approveSummary(@RequestParam Long encounterId, @RequestParam Long doctorId, @RequestParam Long tenantId) {
        aiDoctorService.approveSummary(encounterId, doctorId, tenantId);
        return ResponseEntity.ok().build();
    }
}
