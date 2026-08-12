package com.healthcare.clinic.homevisit.controller;

import com.healthcare.clinic.homevisit.entity.HomeVisitAssignment;
import com.healthcare.clinic.homevisit.entity.HomeVisitRequest;
import com.healthcare.clinic.homevisit.service.HomeVisitDispatcherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/home-visit")
@RequiredArgsConstructor
public class HomeVisitController {

    private final HomeVisitDispatcherService dispatcherService;

    @PostMapping("/requests")
    public ResponseEntity<HomeVisitRequest> createRequest(@RequestBody HomeVisitRequest request) {
        return ResponseEntity.ok(dispatcherService.createRequest(request));
    }

    @GetMapping("/requests")
    public ResponseEntity<List<HomeVisitRequest>> getRequests() {
        return ResponseEntity.ok(dispatcherService.getAllRequests());
    }

    @PostMapping("/requests/{id}/assign")
    public ResponseEntity<HomeVisitAssignment> assignStaff(@PathVariable Long id, @RequestParam Long staffId, @RequestParam Long tenantId) {
        return ResponseEntity.ok(dispatcherService.assignStaff(id, staffId, tenantId));
    }
}
