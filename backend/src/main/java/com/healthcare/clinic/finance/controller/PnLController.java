package com.healthcare.clinic.finance.controller;

import com.healthcare.clinic.finance.dto.PnLResponse;
import com.healthcare.clinic.finance.service.PnLService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/finance/pnl")
@RequiredArgsConstructor
public class PnLController {

    private final PnLService pnLService;

    @GetMapping
    @PreAuthorize("hasAnyRole('FINANCE', 'ADMIN', 'SUPER_ADMIN', 'ACCOUNTANT')")
    public ResponseEntity<PnLResponse> getPnLStatement(
            @RequestParam(required = false) Long branchId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        return ResponseEntity.ok(pnLService.generatePnLStatement(branchId, startDate, endDate));
    }
}
