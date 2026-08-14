package com.healthcare.clinic.patient.controller;

import com.healthcare.clinic.identity.entity.User;
import com.healthcare.clinic.homevisit.entity.HomeVisitRequest;
import com.healthcare.clinic.patient.service.HomeVisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/patient/home-visits")
@RequiredArgsConstructor
public class HomeVisitController {

    private final HomeVisitService homeVisitService;

    @PostMapping
    public ResponseEntity<HomeVisitRequest> requestHomeVisit(
            @AuthenticationPrincipal User user,
            @RequestBody HomeVisitRequest request) {
        return ResponseEntity.ok(homeVisitService.requestHomeVisit(user, request));
    }

    @GetMapping
    public ResponseEntity<List<HomeVisitRequest>> getPatientRequests(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(homeVisitService.getPatientRequests(user));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<HomeVisitRequest> cancelRequest(
            @AuthenticationPrincipal User user,
            @PathVariable Long id) {
        return ResponseEntity.ok(homeVisitService.cancelRequest(user, id));
    }
}
