package com.healthcare.clinic.doctor.controller;

import com.healthcare.clinic.doctor.entity.ClinicalReferral;
import com.healthcare.clinic.doctor.service.ClinicalReferralService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/doctor/referrals")
@RequiredArgsConstructor
public class ClinicalReferralController {

    private final ClinicalReferralService referralService;

    @PostMapping
    public ResponseEntity<ClinicalReferral> createReferral(@RequestBody ClinicalReferral referral) {
        return ResponseEntity.ok(referralService.createReferral(referral));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<ClinicalReferral>> getReferralsForPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(referralService.getReferralsForPatient(patientId));
    }

    @GetMapping("/encounter/{encounterId}")
    public ResponseEntity<List<ClinicalReferral>> getReferralsForEncounter(@PathVariable Long encounterId) {
        return ResponseEntity.ok(referralService.getReferralsForEncounter(encounterId));
    }

    @PutMapping("/{referralId}/status")
    public ResponseEntity<ClinicalReferral> updateReferralStatus(
            @PathVariable Long referralId,
            @RequestParam String status) {
        return ResponseEntity.ok(referralService.updateReferralStatus(referralId, status));
    }
}
