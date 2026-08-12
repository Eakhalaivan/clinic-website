package com.healthcare.clinic.ecommerce.service;

import com.healthcare.clinic.ecommerce.entity.EcReturn;
import com.healthcare.clinic.ecommerce.entity.EcReturnItem;
import com.healthcare.clinic.ecommerce.entity.EcommerceOrder;
import com.healthcare.clinic.ecommerce.repository.EcReturnRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
public class ReturnService {

    private final EcReturnRepository returnRepository;
    private final OrderService orderService;
    private final RefundService refundService;

    @Transactional
    public EcReturn initiateReturn(Long orderId, Long patientId, String reason, String reasonDetail, String evidenceUrls) {
        EcommerceOrder order = orderService.getOrderDetails(orderId, patientId);
        
        if (!"DELIVERED".equals(order.getStatus())) {
            throw new IllegalStateException("Only delivered orders can be returned");
        }

        EcReturn ecReturn = EcReturn.builder()
                .orderId(orderId)
                .requestedBy(patientId)
                .reason(reason)
                .reasonDetail(reasonDetail)
                .evidenceUrls(evidenceUrls)
                .status("REQUESTED")
                .build();
                
        // In reality, map specific order items to EcReturnItem here
        
        return returnRepository.save(ecReturn);
    }

    @Transactional
    public EcReturn approveReturn(Long returnId, Long adminId) {
        EcReturn ecReturn = returnRepository.findById(returnId).orElseThrow();
        ecReturn.setStatus("APPROVED");
        ecReturn.setApprovedBy(adminId);
        ecReturn.setPickupScheduledAt(ZonedDateTime.now().plusDays(1));
        return returnRepository.save(ecReturn);
    }

    @Transactional
    public EcReturn receiveAndInspectReturn(Long returnId, Long inspectorId, boolean isApproved, String notes) {
        EcReturn ecReturn = returnRepository.findById(returnId).orElseThrow();
        ecReturn.setReceivedAt(ZonedDateTime.now());
        ecReturn.setInspectedAt(ZonedDateTime.now());
        ecReturn.setInspectionNotes(notes);

        if (isApproved) {
            ecReturn.setStatus("REFUND_PENDING");
            returnRepository.save(ecReturn);
            
            // Calculate refund amount based on return items (simplified here)
            EcommerceOrder order = orderService.getOrderDetails(ecReturn.getOrderId(), ecReturn.getRequestedBy());
            refundService.initiateRefund(order.getId(), returnId, order.getTotalAmount(), inspectorId);
            
            ecReturn.setStatus("REFUNDED");
            orderService.updateOrderStatus(order.getId(), "RETURNED", inspectorId, "ADMIN", "Return processed and refunded");
        } else {
            ecReturn.setStatus("REJECTED");
            ecReturn.setRejectionReason("Failed inspection: " + notes);
        }
        
        return returnRepository.save(ecReturn);
    }
}
