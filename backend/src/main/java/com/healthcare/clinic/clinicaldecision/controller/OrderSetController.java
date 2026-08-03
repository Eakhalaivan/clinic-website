package com.healthcare.clinic.clinicaldecision.controller;

import com.healthcare.clinic.common.dto.ApiResponse;
import com.healthcare.clinic.clinicaldecision.entity.OrderSetTemplate;
import com.healthcare.clinic.clinicaldecision.service.OrderSetService;
import com.healthcare.clinic.security.SecurityUtils;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/order-sets")
@RequiredArgsConstructor
public class OrderSetController {

    private final OrderSetService orderSetService;

    @GetMapping
    @PreAuthorize("hasAnyRole('DOCTOR', 'NURSE', 'ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<OrderSetTemplate>>> getOrderSets(
            @RequestParam(required = false) String diagnosisCode) {
        return ResponseEntity.ok(ApiResponse.success(orderSetService.getOrderSetsForDiagnosis(diagnosisCode)));
    }

    @PostMapping("/apply/{templateId}")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> applyOrderSet(
            @PathVariable Long templateId,
            @RequestBody ApplyOrderSetRequest request) {
        Long doctorId = SecurityUtils.getCurrentUserId();
        Map<String, Object> result = orderSetService.applyOrderSet(templateId, request.getPatientId(), doctorId);
        return ResponseEntity.ok(ApiResponse.success(result, "Order set applied successfully"));
    }
}

@Data
class ApplyOrderSetRequest {
    private Long patientId;
}
