package com.healthcare.clinic.finance.service;

import com.healthcare.clinic.finance.entity.Expense;
import com.healthcare.clinic.finance.entity.InsuranceClaim;
import com.healthcare.clinic.finance.entity.Payment;
import com.healthcare.clinic.finance.repository.ExpenseRepository;
import com.healthcare.clinic.finance.repository.InsuranceClaimRepository;
import com.healthcare.clinic.finance.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FinanceService {

    private final PaymentRepository paymentRepository;
    private final ExpenseRepository expenseRepository;
    private final InsuranceClaimRepository insuranceClaimRepository;

    @Transactional
    public Payment recordPayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    @Transactional(readOnly = true)
    public List<Payment> getAllPayments() {
        return paymentRepository.findAllByOrderByPaidAtDesc();
    }

    @Transactional
    public Expense recordExpense(Expense expense) {
        return expenseRepository.save(expense);
    }

    @Transactional(readOnly = true)
    public List<Expense> getAllExpenses() {
        return expenseRepository.findAllByOrderByIncurredOnDesc();
    }

    @Transactional
    public InsuranceClaim submitClaim(InsuranceClaim claim) {
        return insuranceClaimRepository.save(claim);
    }

    @Transactional
    public InsuranceClaim updateClaimStatus(Long claimId, String status) {
        InsuranceClaim claim = insuranceClaimRepository.findById(claimId).orElseThrow();
        claim.setStatus(status);
        if ("SETTLED".equals(status) || "APPROVED".equals(status)) {
            claim.setSettledAt(ZonedDateTime.now());
        }
        return insuranceClaimRepository.save(claim);
    }

    @Transactional(readOnly = true)
    public List<InsuranceClaim> getAllClaims() {
        return insuranceClaimRepository.findAllByOrderBySubmittedAtDesc();
    }
}
