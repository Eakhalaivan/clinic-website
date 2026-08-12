package com.healthcare.clinic.reception.controller;

import com.healthcare.clinic.reception.entity.QueueToken;
import com.healthcare.clinic.reception.entity.QueueTransfer;
import com.healthcare.clinic.reception.repository.QueueTokenRepository;
import com.healthcare.clinic.reception.repository.QueueTransferRepository;
import com.healthcare.clinic.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reception/queue")
@RequiredArgsConstructor
public class QueueManagementController {

    private final QueueTokenRepository queueTokenRepository;
    private final QueueTransferRepository queueTransferRepository;

    @GetMapping("/branch/{branchId}")
    @PreAuthorize("hasAuthority('ROLE_RECEPTION') or hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_NURSE') or hasAuthority('ROLE_DOCTOR')")
    public ResponseEntity<List<QueueToken>> getQueueForBranch(@PathVariable Long branchId) {
        java.time.ZonedDateTime startOfDay = java.time.ZonedDateTime.now().toLocalDate().atStartOfDay(java.time.ZoneId.systemDefault());
        return ResponseEntity.ok(queueTokenRepository.findByBranchIdAndGeneratedAtAfterOrderByPriorityLevelDescTokenNumberAsc(branchId, startOfDay));
    }

    @PostMapping("/{tokenId}/transfer")
    @PreAuthorize("hasAuthority('ROLE_RECEPTION') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<QueueTransfer> transferToken(
            @PathVariable Long tokenId,
            @RequestBody Map<String, Object> request) {
        
        QueueToken token = queueTokenRepository.findById(tokenId)
                .orElseThrow(() -> new IllegalArgumentException("Token not found"));
                
        Long fromDoctorId = request.containsKey("fromDoctorId") && request.get("fromDoctorId") != null ? Long.valueOf(request.get("fromDoctorId").toString()) : null;
        Long toDoctorId = request.containsKey("toDoctorId") && request.get("toDoctorId") != null ? Long.valueOf(request.get("toDoctorId").toString()) : null;
        String reason = (String) request.get("reason");
        String department = (String) request.get("department");

        QueueTransfer transfer = QueueTransfer.builder()
                .tokenId(tokenId)
                .fromDoctorId(fromDoctorId)
                .toDoctorId(toDoctorId)
                .reason(reason)
                .transferredByUserId(SecurityUtils.getCurrentUserId())
                .build();
                
        if (department != null) {
            token.setCurrentDepartment(department);
            queueTokenRepository.save(token);
        }

        return ResponseEntity.ok(queueTransferRepository.save(transfer));
    }

    @PutMapping("/{tokenId}/status")
    @PreAuthorize("hasAuthority('ROLE_RECEPTION') or hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_DOCTOR')")
    public ResponseEntity<QueueToken> updateTokenStatus(
            @PathVariable Long tokenId,
            @RequestBody Map<String, String> request) {
        QueueToken token = queueTokenRepository.findById(tokenId)
                .orElseThrow(() -> new IllegalArgumentException("Token not found"));
                
        String status = request.get("status");
        if (status == null) throw new IllegalArgumentException("Status is required");
        
        token.setStatus(status);
        return ResponseEntity.ok(queueTokenRepository.save(token));
    }
}
