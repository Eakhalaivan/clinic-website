package com.healthcare.clinic.marketing.service;

import com.healthcare.clinic.marketing.entity.GiftCard;
import com.healthcare.clinic.marketing.repository.GiftCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.ZonedDateTime;
import java.util.HexFormat;

@Service
@RequiredArgsConstructor
public class GiftCardService {

    private final GiftCardRepository giftCardRepository;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final String ALPHABET = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"; // no ambiguous chars

    /**
     * Issues a new gift card with server-generated code.
     * Returns the plaintext code ONCE — it is then hashed and only the suffix is stored.
     */
    @Transactional
    public GiftCardIssueResult issueGiftCard(BigDecimal amount, Long issuedToPatientId,
                                              Long purchasedByPatientId, Long purchaseInvoiceId,
                                              Long branchId, ZonedDateTime expiresAt) {
        String plainCode = generateCode(16);
        String codeHash = hash(plainCode);
        String codeSuffix = plainCode.substring(plainCode.length() - 4);

        GiftCard card = GiftCard.builder()
                .codeHash(codeHash)
                .codeSuffix(codeSuffix)
                .initialBalance(amount)
                .currentBalance(amount)
                .issuedToPatientId(issuedToPatientId)
                .purchasedByPatientId(purchasedByPatientId)
                .purchaseInvoiceId(purchaseInvoiceId)
                .branchId(branchId)
                .status("ACTIVE")
                .activatedAt(ZonedDateTime.now())
                .expiresAt(expiresAt)
                .redemptionCount(0)
                .build();

        GiftCard saved = giftCardRepository.save(card);
        return new GiftCardIssueResult(saved, plainCode); // plaintext returned once only
    }

    /**
     * Checks balance for a gift card. Rate limiting is enforced at the controller layer.
     */
    @Transactional(readOnly = true)
    public GiftCard getBalance(String plainCode) {
        String codeHash = hash(plainCode);
        return giftCardRepository.findByCodeHash(codeHash)
                .orElseThrow(() -> new IllegalArgumentException("Gift card not found"));
    }

    /**
     * Redeems amount from gift card. Uses pessimistic lock to prevent concurrent double-redemption.
     */
    @Transactional
    public GiftCard redeem(String plainCode, BigDecimal amount, Long invoiceId) {
        String codeHash = hash(plainCode);
        GiftCard card = giftCardRepository.findWithLockByCodeHash(codeHash)
                .orElseThrow(() -> new IllegalArgumentException("Gift card not found"));

        if (!"ACTIVE".equals(card.getStatus())) {
            throw new IllegalStateException("Gift card is not active. Status: " + card.getStatus());
        }
        if (card.getExpiresAt() != null && card.getExpiresAt().isBefore(ZonedDateTime.now())) {
            card.setStatus("EXPIRED");
            giftCardRepository.save(card);
            throw new IllegalStateException("Gift card has expired");
        }
        if (card.getCurrentBalance().compareTo(amount) < 0) {
            throw new IllegalStateException("Insufficient gift card balance. Available: "
                    + card.getCurrentBalance() + ", requested: " + amount);
        }

        BigDecimal newBalance = card.getCurrentBalance().subtract(amount);
        card.setCurrentBalance(newBalance);
        card.setRedemptionCount(card.getRedemptionCount() + 1);
        if (newBalance.compareTo(BigDecimal.ZERO) == 0) {
            card.setStatus("REDEEMED");
        }

        return giftCardRepository.save(card);
    }

    @Transactional
    public GiftCard cancelGiftCard(String plainCode, Long cancelledBy) {
        String codeHash = hash(plainCode);
        GiftCard card = giftCardRepository.findByCodeHash(codeHash)
                .orElseThrow(() -> new IllegalArgumentException("Gift card not found"));
        if ("REDEEMED".equals(card.getStatus()) || "CANCELLED".equals(card.getStatus())) {
            throw new IllegalStateException("Cannot cancel a " + card.getStatus() + " gift card");
        }
        card.setStatus("CANCELLED");
        card.setCancelledAt(ZonedDateTime.now());
        return giftCardRepository.save(card);
    }

    // ─── Private helpers ──────────────────────────────────────────────────────

    private String generateCode(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(ALPHABET.charAt(SECURE_RANDOM.nextInt(ALPHABET.length())));
        }
        return sb.toString();
    }

    private String hash(String plainCode) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] h = digest.digest(plainCode.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(h);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }

    public record GiftCardIssueResult(GiftCard card, String plainCode) {}
}
