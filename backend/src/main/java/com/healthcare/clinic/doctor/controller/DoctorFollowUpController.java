package com.healthcare.clinic.doctor.controller;

import com.healthcare.clinic.doctor.dto.FollowUpResponse;
import com.healthcare.clinic.doctor.service.DoctorFollowUpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.healthcare.clinic.security.SecurityUtils;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/doctor/follow-ups")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_DOCTOR')")
public class DoctorFollowUpController {

    private final DoctorFollowUpService followUpService;

    @GetMapping
    public ResponseEntity<List<FollowUpResponse>> getFollowUps() {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        // optionally call updateStatuses here or via a scheduled job
        followUpService.updateStatuses();
        return ResponseEntity.ok(followUpService.getFollowUpsForDoctor(currentUserId));
    }
}
