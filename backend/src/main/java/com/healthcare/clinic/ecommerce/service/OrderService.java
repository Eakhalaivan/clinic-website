package com.healthcare.clinic.ecommerce.service;

import com.healthcare.clinic.ecommerce.entity.EcOrderStatusHistory;
import com.healthcare.clinic.ecommerce.entity.EcommerceOrder;
import com.healthcare.clinic.ecommerce.repository.EcOrderStatusHistoryRepository;
import com.healthcare.clinic.ecommerce.repository.EcommerceOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final EcommerceOrderRepository orderRepository;
    private final EcOrderStatusHistoryRepository statusHistoryRepository;

    @Transactional(readOnly = true)
    public List<EcommerceOrder> getPatientOrders(Long patientId) {
        return orderRepository.findAll().stream()
                .filter(o -> patientId.equals(o.getPatientId()))
                .toList();
    }
    
    @Transactional(readOnly = true)
    public List<EcommerceOrder> getAllOrders() {
        return orderRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public EcommerceOrder getOrderDetailsForAdmin(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow();
    }
    
    @Transactional
    public void updateShipping(Long orderId, String status, String trackingNumber) {
        EcommerceOrder order = orderRepository.findById(orderId).orElseThrow();
        order.setStatus(status);
        order.setTrackingNumber(trackingNumber);
        
        if ("SHIPPED".equals(status)) {
            order.setShippedAt(java.time.ZonedDateTime.now());
        } else if ("DELIVERED".equals(status)) {
            order.setDeliveredAt(java.time.ZonedDateTime.now());
        }
        
        orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public EcommerceOrder getOrderDetails(Long orderId, Long patientId) {
        EcommerceOrder order = orderRepository.findById(orderId).orElseThrow();
        if (!order.getPatientId().equals(patientId)) {
            throw new SecurityException("Unauthorized access to order");
        }
        return order;
    }

    @Transactional
    public void updateOrderStatus(Long orderId, String newStatus, Long actorId, String actorRole, String note) {
        EcommerceOrder order = orderRepository.findById(orderId).orElseThrow();
        order.setStatus(newStatus);
        orderRepository.save(order);

        statusHistoryRepository.save(EcOrderStatusHistory.builder()
                .orderId(orderId)
                .status(newStatus)
                .actorId(actorId)
                .actorRole(actorRole)
                .note(note)
                .build());
    }
}
