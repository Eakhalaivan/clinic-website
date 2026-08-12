package com.healthcare.clinic.nursing.controller;

import com.healthcare.clinic.nursing.entity.WardTransfer;
import com.healthcare.clinic.nursing.service.WardTransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/nursing/transfers")
@RequiredArgsConstructor
public class WardTransferController {

    private final WardTransferService wardTransferService;

    @PostMapping("/request")
    @PreAuthorize("hasAnyRole('NURSE', 'CHARGE_NURSE', 'DOCTOR', 'SUPER_ADMIN')")
    public ResponseEntity<WardTransfer> requestTransfer(
            @RequestParam Long patientId,
            @RequestParam Long encounterId,
            @RequestParam(required = false) Long destinationBedId,
            @RequestParam(defaultValue = "ROUTINE") String priority,
            @RequestParam String reason) {
        return ResponseEntity.ok(wardTransferService.requestTransfer(patientId, encounterId, destinationBedId, priority, reason));
    }

    @PostMapping("/{transferId}/approve")
    @PreAuthorize("hasAnyRole('CHARGE_NURSE', 'SUPER_ADMIN')")
    public ResponseEntity<WardTransfer> approveTransfer(
            @PathVariable Long transferId,
            @RequestParam(required = false) Long approvedBedId) {
        return ResponseEntity.ok(wardTransferService.approveTransfer(transferId, approvedBedId));
    }

    @PostMapping("/{transferId}/complete")
    @PreAuthorize("hasAnyRole('CHARGE_NURSE', 'NURSE', 'SUPER_ADMIN')")
    public ResponseEntity<WardTransfer> completeTransfer(@PathVariable Long transferId) {
        return ResponseEntity.ok(wardTransferService.completeTransfer(transferId));
    }
}
