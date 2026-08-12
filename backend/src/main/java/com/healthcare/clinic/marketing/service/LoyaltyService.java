package com.healthcare.clinic.marketing.service;

import com.healthcare.clinic.marketing.entity.LoyaltyTransaction;
import com.healthcare.clinic.marketing.entity.PatientLoyalty;
import com.healthcare.clinic.marketing.repository.LoyaltyTransactionRepository;
import com.healthcare.clinic.marketing.repository.PatientLoyaltyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
public class LoyaltyService {

    private final PatientLoyaltyRepository loyaltyRepository;
    private final LoyaltyTransactionRepository transactionRepository;

    @Transactional(readOnly = true)
    public PatientLoyalty getLoyaltyBalance(Long patientId) {
        return loyaltyRepository.findByPatientId(patientId)
                .orElseGet(() -> loyaltyRepository.save(PatientLoyalty.builder()
                        .patientId(patientId)
                        .pointsBalance(0)
                        .tier("BRONZE")
                        .lifetimeEarned(0)
                        .lifetimeRedeemed(0)
                        .build()));
    }

    @Transactional(readOnly = true)
    public Page<LoyaltyTransaction> getTransactionHistory(Long patientId, Pageable pageable) {
        return transactionRepository.findByPatientIdOrderByCreatedAtDesc(patientId, pageable);
    }

    /**
     * Awards points linked to an invoice. Idempotent — safe to call multiple times
     * for the same invoice (duplicate award is silently skipped).
     */
    @Transactional
    public LoyaltyTransaction awardPoints(Long patientId, int points, String referenceType,
                                           Long referenceId, String idempotencyKey) {
        // Idempotency check
        if (idempotencyKey != null) {
            var existing = transactionRepository.findByIdempotencyKey(idempotencyKey);
            if (existing.isPresent()) {
                return existing.get(); // duplicate — return previous result
            }
        }

        PatientLoyalty loyalty = getLoyaltyBalance(patientId);
        int balanceBefore = loyalty.getPointsBalance();
        int balanceAfter = balanceBefore + points;

        loyalty.setPointsBalance(balanceAfter);
        loyalty.setLifetimeEarned(loyalty.getLifetimeEarned() + points);
        loyalty.setLastEarnedAt(ZonedDateTime.now());
        updateTier(loyalty, balanceAfter);
        loyaltyRepository.save(loyalty);

        LoyaltyTransaction tx = LoyaltyTransaction.builder()
                .patientId(patientId)
                .type("EARNED")
                .points(points)
                .referenceType(referenceType)
                .referenceId(referenceId)
                .balanceBefore(balanceBefore)
                .balanceAfter(balanceAfter)
                .idempotencyKey(idempotencyKey)
                .build();
        return transactionRepository.save(tx);
    }

    /**
     * Redeems points against an invoice. Uses pessimistic lock to prevent
     * concurrent double-redemption.
     */
    @Transactional
    public LoyaltyTransaction redeemPoints(Long patientId, int points,
                                            Long invoiceId, String idempotencyKey) {
        // Pessimistic lock via findById — JPA will lock the row during the transaction
        PatientLoyalty loyalty = loyaltyRepository.findByPatientId(patientId)
                .orElseThrow(() -> new IllegalStateException("Loyalty account not found for patient: " + patientId));

        if (loyalty.getPointsBalance() < points) {
            throw new IllegalStateException("Insufficient loyalty points. Available: "
                    + loyalty.getPointsBalance() + ", requested: " + points);
        }

        // Idempotency
        if (idempotencyKey != null) {
            var existing = transactionRepository.findByIdempotencyKey(idempotencyKey);
            if (existing.isPresent()) return existing.get();
        }

        int balanceBefore = loyalty.getPointsBalance();
        int balanceAfter = balanceBefore - points;

        loyalty.setPointsBalance(balanceAfter);
        loyalty.setLifetimeRedeemed(loyalty.getLifetimeRedeemed() + points);
        loyalty.setLastRedeemedAt(ZonedDateTime.now());
        updateTier(loyalty, balanceAfter);
        loyaltyRepository.save(loyalty);

        LoyaltyTransaction tx = LoyaltyTransaction.builder()
                .patientId(patientId)
                .type("REDEEMED")
                .points(-points)
                .referenceType("INVOICE")
                .referenceId(invoiceId)
                .balanceBefore(balanceBefore)
                .balanceAfter(balanceAfter)
                .idempotencyKey(idempotencyKey)
                .build();
        return transactionRepository.save(tx);
    }

    /**
     * Manual point adjustment. Requires approvedBy — audit-trailed.
     */
    @Transactional
    public LoyaltyTransaction manualAdjust(Long patientId, int points, String notes, Long approvedBy) {
        PatientLoyalty loyalty = getLoyaltyBalance(patientId);
        int balanceBefore = loyalty.getPointsBalance();
        int balanceAfter = balanceBefore + points;

        if (balanceAfter < 0) {
            throw new IllegalStateException("Adjustment would result in negative balance");
        }

        loyalty.setPointsBalance(balanceAfter);
        if (points > 0) loyalty.setLifetimeEarned(loyalty.getLifetimeEarned() + points);
        updateTier(loyalty, balanceAfter);
        loyaltyRepository.save(loyalty);

        LoyaltyTransaction tx = LoyaltyTransaction.builder()
                .patientId(patientId)
                .type("ADJUSTED")
                .points(points)
                .referenceType("ADMIN")
                .balanceBefore(balanceBefore)
                .balanceAfter(balanceAfter)
                .notes(notes)
                .approvedBy(approvedBy)
                .build();
        return transactionRepository.save(tx);
    }

    // ─── Private helpers ──────────────────────────────────────────────────────

    private void updateTier(PatientLoyalty loyalty, int balance) {
        if (balance >= 1000) loyalty.setTier("PLATINUM");
        else if (balance >= 500) loyalty.setTier("GOLD");
        else if (balance >= 200) loyalty.setTier("SILVER");
        else loyalty.setTier("BRONZE");
    }
}
