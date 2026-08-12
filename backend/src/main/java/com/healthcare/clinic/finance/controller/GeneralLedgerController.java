package com.healthcare.clinic.finance.controller;

import com.healthcare.clinic.finance.entity.JournalEntry;
import com.healthcare.clinic.finance.service.GeneralLedgerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/finance/ledger")
@RequiredArgsConstructor
public class GeneralLedgerController {

    private final GeneralLedgerService generalLedgerService;

    @PostMapping("/journals")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT')")
    public ResponseEntity<JournalEntry> postJournal(@RequestBody Map<String, Object> request) {
        String description = (String) request.get("description");
        LocalDate entryDate = LocalDate.parse((String) request.get("entryDate"));
        String referenceType = (String) request.get("referenceType");
        
        Long referenceId = request.get("referenceId") != null ? Long.valueOf(request.get("referenceId").toString()) : null;
        Long debitAccountId = Long.valueOf(request.get("debitAccountId").toString());
        BigDecimal debitAmount = new BigDecimal(request.get("debitAmount").toString());
        Long creditAccountId = Long.valueOf(request.get("creditAccountId").toString());
        BigDecimal creditAmount = new BigDecimal(request.get("creditAmount").toString());
        
        Long branchId = request.get("branchId") != null ? Long.valueOf(request.get("branchId").toString()) : null;
        Long preparedBy = request.get("preparedBy") != null ? Long.valueOf(request.get("preparedBy").toString()) : null;

        JournalEntry entry = generalLedgerService.postDoubleEntry(
                description, entryDate, referenceType, referenceId,
                debitAccountId, debitAmount,
                creditAccountId, creditAmount,
                branchId, preparedBy);

        return ResponseEntity.ok(entry);
    }
    
    @GetMapping("/journals")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT')")
    public ResponseEntity<List<JournalEntry>> getJournals() {
        return ResponseEntity.ok(generalLedgerService.getAllJournals());
    }
}
