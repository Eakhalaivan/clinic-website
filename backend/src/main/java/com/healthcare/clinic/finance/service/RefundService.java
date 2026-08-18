package com.healthcare.clinic.finance.service;

import com.healthcare.clinic.billing.entity.Invoice;
import com.healthcare.clinic.billing.repository.InvoiceRepository;
import com.healthcare.clinic.finance.entity.Payment;
import com.healthcare.clinic.finance.entity.Refund;
import com.healthcare.clinic.finance.entity.RefundStatus;
import com.healthcare.clinic.finance.repository.PaymentRepository;
import com.healthcare.clinic.finance.repository.RefundRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefundService {

    private final RefundRepository refundRepository;
    private final PaymentRepository paymentRepository;
    private final InvoiceRepository invoiceRepository;

    public List<Refund> getAllRefunds() {
        return refundRepository.findAll();
    }

    @Transactional
    public Refund initiateRefund(Long paymentId, BigDecimal amount, String reason, Long requestedBy, String idempotencyKey) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));

        // Check total refunded so far
        BigDecimal totalRefunded = refundRepository.findByOriginalPaymentId(paymentId).stream()
                .filter(r -> r.getStatus() != RefundStatus.REJECTED && r.getStatus() != RefundStatus.FAILED)
                .map(Refund::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalRefunded.add(amount).compareTo(payment.getAmount()) > 0) {
            throw new IllegalStateException("Refund amount exceeds available payment balance");
        }

        Refund refund = Refund.builder()
                .refundReference("REF-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .originalPayment(payment)
                .amount(amount)
                .refundReason(reason)
                .requestedBy(requestedBy)
                .idempotencyKey(idempotencyKey)
                .status(RefundStatus.INITIATED)
                .build();

        return refundRepository.save(refund);
    }

    @Transactional
    public Refund approveRefund(Long refundId, Long approvedBy) {
        Refund refund = refundRepository.findById(refundId)
                .orElseThrow(() -> new IllegalArgumentException("Refund not found"));

        if (refund.getStatus() != RefundStatus.INITIATED && refund.getStatus() != RefundStatus.PENDING_APPROVAL) {
            throw new IllegalStateException("Refund is not in a state to be approved");
        }

        refund.setStatus(RefundStatus.APPROVED);
        refund.setApprovedBy(approvedBy);
        return refundRepository.save(refund);
    }

    @Transactional
    public Refund processRefund(Long refundId) {
        Refund refund = refundRepository.findById(refundId)
                .orElseThrow(() -> new IllegalArgumentException("Refund not found"));

        if (refund.getStatus() != RefundStatus.APPROVED) {
            throw new IllegalStateException("Refund must be approved before processing");
        }

        // Ideally, here we'd integrate with the payment gateway (e.g. Stripe) to perform actual refund
        // Since we're mocking the external part:
        refund.setStatus(RefundStatus.PROCESSED);
        
        // Also reduce the invoice's amount paid if the original payment was allocated
        // In this simple model we're just recording it. In full accounting we'd issue a Credit Note.
        return refundRepository.save(refund);
    }
}
