package com.healthcare.clinic.emr.controller;

import com.healthcare.clinic.emr.dto.ChartSummaryDTO;
import com.healthcare.clinic.emr.service.ChartSummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/emr/patients")
@RequiredArgsConstructor
public class ChartSummaryController {

    private final ChartSummaryService chartSummaryService;

    @GetMapping("/{patientId}/chart-summary")
    public ResponseEntity<ChartSummaryDTO> getChartSummary(@PathVariable Long patientId) {
        return ResponseEntity.ok(chartSummaryService.getChartSummary(patientId));
    }
}
