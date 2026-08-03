package com.healthcare.clinic.finance.controller;

import com.healthcare.clinic.finance.entity.Expense;
import com.healthcare.clinic.finance.entity.InsuranceClaim;
import com.healthcare.clinic.finance.entity.Payment;
import com.healthcare.clinic.finance.service.FinanceService;
import com.healthcare.clinic.identity.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/finance")
@RequiredArgsConstructor
@PreAuthorize("hasRole('FINANCE') or hasRole('ACCOUNTANT') or hasRole('SUPER_ADMIN') or hasRole('ADMIN')")
public class FinanceController {

    private final FinanceService financeService;

    @GetMapping("/payments")
    public ResponseEntity<List<Payment>> getPayments() {
        return ResponseEntity.ok(financeService.getAllPayments());
    }

    @PostMapping("/payments")
    public ResponseEntity<Payment> recordPayment(@RequestBody Payment payment, @AuthenticationPrincipal User user) {
        payment.setRecordedBy(user.getId());
        return ResponseEntity.ok(financeService.recordPayment(payment));
    }

    @GetMapping("/expenses")
    public ResponseEntity<List<Expense>> getExpenses() {
        return ResponseEntity.ok(financeService.getAllExpenses());
    }

    @PostMapping("/expenses")
    public ResponseEntity<Expense> recordExpense(@RequestBody Expense expense, @AuthenticationPrincipal User user) {
        expense.setRecordedBy(user.getId());
        return ResponseEntity.ok(financeService.recordExpense(expense));
    }

    @GetMapping("/claims")
    public ResponseEntity<List<InsuranceClaim>> getClaims() {
        return ResponseEntity.ok(financeService.getAllClaims());
    }

    @PostMapping("/claims")
    public ResponseEntity<InsuranceClaim> submitClaim(@RequestBody InsuranceClaim claim) {
        return ResponseEntity.ok(financeService.submitClaim(claim));
    }

    @PatchMapping("/claims/{id}/status")
    public ResponseEntity<InsuranceClaim> updateClaimStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(financeService.updateClaimStatus(id, status));
    }
}
