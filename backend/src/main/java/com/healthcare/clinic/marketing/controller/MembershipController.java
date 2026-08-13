package com.healthcare.clinic.marketing.controller;

import com.healthcare.clinic.marketing.entity.MembershipPlan;
import com.healthcare.clinic.marketing.entity.PatientMembership;
import com.healthcare.clinic.marketing.repository.MembershipPlanRepository;
import com.healthcare.clinic.marketing.repository.PatientMembershipRepository;
import com.healthcare.clinic.patient.entity.PatientProfile;
import com.healthcare.clinic.patient.repository.PatientProfileRepository;
import com.healthcare.clinic.security.SecurityUtils;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/marketing/membership")
@RequiredArgsConstructor
public class MembershipController {

    private final MembershipPlanRepository planRepository;
    private final PatientMembershipRepository patientMembershipRepository;
    private final PatientProfileRepository patientProfileRepository;

    @GetMapping("/plans")
    public ResponseEntity<List<MembershipPlan>> getActivePlans() {
        return ResponseEntity.ok(planRepository.findByActiveTrue());
    }

    @PostMapping("/plans")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPER_ADMIN') or hasAuthority('ROLE_MARKETING')")
    public ResponseEntity<MembershipPlan> createPlan(@RequestBody MembershipPlan plan) {
        return ResponseEntity.ok(planRepository.save(plan));
    }

    @GetMapping("/my-membership")
    @PreAuthorize("hasAuthority('ROLE_PATIENT')")
    public ResponseEntity<PatientMembership> getMyMembership() {
        Long userId = SecurityUtils.getCurrentUserId();
        PatientProfile profile = patientProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient profile not found"));
        
        return patientMembershipRepository.findTopByPatientIdAndStatusOrderByEndDateDesc(profile.getId(), "ACTIVE")
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No active membership found"));
    }

    @PostMapping("/subscribe")
    @PreAuthorize("hasAuthority('ROLE_PATIENT')")
    public ResponseEntity<PatientMembership> subscribe(@RequestBody SubscribeRequest request) {
        Long userId = SecurityUtils.getCurrentUserId();
        PatientProfile profile = patientProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient profile not found"));

        MembershipPlan plan = planRepository.findById(request.getPlanId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Plan not found"));

        if (!"ACTIVE".equals(plan.getStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Plan is not active");
        }
        
        // Mock payment process here...

        java.time.LocalDate now = java.time.LocalDate.now();
        java.time.LocalDate endDate = "YEARLY".equalsIgnoreCase(request.getBillingCycle()) ? now.plusYears(1) : now.plusMonths(1);

        PatientMembership membership = PatientMembership.builder()
                .patientId(profile.getId())
                .planId(plan.getId())
                .startDate(now)
                .endDate(endDate)
                .status("ACTIVE")
                .build();

        return ResponseEntity.ok(patientMembershipRepository.save(membership));
    }

    @Data
    public static class SubscribeRequest {
        private Long planId;
        private String billingCycle; // MONTHLY, YEARLY
    }
}
