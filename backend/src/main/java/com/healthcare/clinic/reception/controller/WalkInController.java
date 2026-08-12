package com.healthcare.clinic.reception.controller;

import com.healthcare.clinic.reception.entity.WalkInRegistration;
import com.healthcare.clinic.reception.service.WalkInRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/reception/walk-in")
@RequiredArgsConstructor
public class WalkInController {

    private final WalkInRegistrationService walkInRegistrationService;

    @PostMapping("/branch/{branchId}/register")
    @PreAuthorize("hasAuthority('ROLE_RECEPTION') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<WalkInRegistration> registerWalkIn(
            @PathVariable Long branchId,
            @RequestBody Map<String, Object> request) {
        
        Long patientId = request.containsKey("patientId") && request.get("patientId") != null 
                ? Long.valueOf(request.get("patientId").toString()) 
                : null;
        String reason = (String) request.get("reasonForVisit");
        Integer priorityLevel = request.containsKey("priorityLevel") && request.get("priorityLevel") != null 
                ? Integer.valueOf(request.get("priorityLevel").toString()) 
                : 0;
        String department = (String) request.get("department");

        WalkInRegistration walkIn = walkInRegistrationService.registerWalkIn(branchId, patientId, reason, priorityLevel, department);
        return ResponseEntity.ok(walkIn);
    }
}
