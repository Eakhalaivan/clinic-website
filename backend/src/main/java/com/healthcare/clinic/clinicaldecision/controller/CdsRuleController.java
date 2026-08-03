package com.healthcare.clinic.clinicaldecision.controller;

import com.healthcare.clinic.common.dto.ApiResponse;
import com.healthcare.clinic.clinicaldecision.entity.CdsRule;
import com.healthcare.clinic.clinicaldecision.entity.TriggerEvent;
import com.healthcare.clinic.clinicaldecision.service.CdsRuleService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cds/rules")
@RequiredArgsConstructor
public class CdsRuleController {

    private final CdsRuleService ruleService;

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'BRANCH_ADMIN')")
    public ResponseEntity<ApiResponse<CdsRule>> createOrUpdateRule(@RequestBody CdsRule rule) {
        return ResponseEntity.ok(ApiResponse.success(ruleService.createOrUpdateRule(rule), "CDS Rule saved successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CdsRule>>> getActiveRules() {
        return ResponseEntity.ok(ApiResponse.success(ruleService.getActiveRules()));
    }

    @PostMapping("/evaluate")
    public ResponseEntity<ApiResponse<Map<String, Object>>> evaluateRules(@RequestBody EvaluateRequest request) {
        TriggerEvent event = TriggerEvent.ON_PRESCRIPTION;
        try {
            if (request.getTriggerEvent() != null) {
                event = TriggerEvent.valueOf(request.getTriggerEvent().toUpperCase());
            }
        } catch (Exception ignored) {}

        Map<String, Object> result = ruleService.evaluateRules(request.getPatientId(), event, request.getItems());
        return ResponseEntity.ok(ApiResponse.success(result));
    }
}

@Data
class EvaluateRequest {
    private Long patientId;
    private String triggerEvent;
    private List<String> items;
}
