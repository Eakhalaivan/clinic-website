package com.healthcare.clinic.ecommerce.service;

import com.healthcare.clinic.ecommerce.entity.EcFulfillmentTask;
import com.healthcare.clinic.ecommerce.entity.EcommerceOrder;
import com.healthcare.clinic.ecommerce.repository.EcFulfillmentTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FulfillmentService {

    private final EcFulfillmentTaskRepository fulfillmentTaskRepository;
    private final OrderService orderService;
    private final DeliveryService deliveryService;

    @Transactional
    public EcFulfillmentTask createFulfillmentTask(EcommerceOrder order) {
        if (!"CONFIRMED".equals(order.getStatus())) {
            throw new IllegalStateException("Order must be confirmed to start fulfillment");
        }

        return fulfillmentTaskRepository.save(EcFulfillmentTask.builder()
                .orderId(order.getId())
                .status("PENDING")
                .prescriptionVerified(!order.getPrescriptionReviewRequired())
                .build());
    }

    @Transactional
    public EcFulfillmentTask assignTask(Long taskId, Long pharmacistId) {
        EcFulfillmentTask task = fulfillmentTaskRepository.findById(taskId).orElseThrow();
        task.setAssignedTo(pharmacistId);
        task.setStatus("IN_PROGRESS");
        task.setStartedAt(ZonedDateTime.now());
        return fulfillmentTaskRepository.save(task);
    }

    @Transactional
    public EcFulfillmentTask verifyPrescription(Long taskId, Long pharmacistId) {
        EcFulfillmentTask task = fulfillmentTaskRepository.findById(taskId).orElseThrow();
        task.setPrescriptionVerified(true);
        task.setPrescriptionVerifiedBy(pharmacistId);
        task.setPrescriptionVerifiedAt(ZonedDateTime.now());
        return fulfillmentTaskRepository.save(task);
    }

    @Transactional
    public void markPacked(Long taskId, Long pharmacistId, String packingEvidenceUrl) {
        EcFulfillmentTask task = fulfillmentTaskRepository.findById(taskId).orElseThrow();
        if (!task.getPrescriptionVerified()) {
            throw new IllegalStateException("Cannot pack unverified prescription order");
        }
        
        task.setStatus("PACKED");
        task.setPackingEvidenceUrl(packingEvidenceUrl);
        task.setCompletedAt(ZonedDateTime.now());
        fulfillmentTaskRepository.save(task);

        orderService.updateOrderStatus(task.getOrderId(), "PACKED", pharmacistId, "PHARMACIST", "Order packed");
        
        // Hand off to delivery
        deliveryService.createShipment(task.getOrderId());
    }
}
