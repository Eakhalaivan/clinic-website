package com.healthcare.clinic.ecommerce.service;

import com.healthcare.clinic.ecommerce.entity.EcRefund;
import com.healthcare.clinic.ecommerce.entity.EcReturn;
import com.healthcare.clinic.ecommerce.repository.EcRefundRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Slf4j
@Service("ecommerceRefundService")
@RequiredArgsConstructor
public class RefundService {

    private final EcRefundRepository refundRepository;
    private final OrderService orderService;

    @Transactional
    public EcRefund initiateRefund(Long orderId, Long returnId, BigDecimal amount, Long approvedBy) {
        String idempotencyKey = "REF_" + orderId + "_" + (returnId != null ? returnId : "CANC") + "_" + UUID.randomUUID().toString();
        
        EcRefund refund = EcRefund.builder()
                .orderId(orderId)
                .returnId(returnId)
                .idempotencyKey(idempotencyKey)
                .amount(amount)
                .method("ORIGINAL")
                .status("PROCESSING")
                .approvedBy(approvedBy)
                .build();
                
        refund = refundRepository.save(refund);

        // Mock payment gateway refund logic
        try {
            log.info("Mock processing refund of {} for order {}", amount, orderId);
            refund.setStatus("SUCCESSFUL");
            refund.setProviderRef("MOCK_REF_" + UUID.randomUUID().toString().substring(0, 8));
            refund.setProcessedAt(ZonedDateTime.now());
            
            if (returnId == null) {
                // If this is a direct cancellation refund, update order status
                orderService.updateOrderStatus(orderId, "REFUNDED", approvedBy, "SYSTEM", "Refund processed successfully");
            }
        } catch (Exception e) {
            refund.setStatus("FAILED");
            refund.setFailureReason(e.getMessage());
        }
        
        return refundRepository.save(refund);
    }
}
