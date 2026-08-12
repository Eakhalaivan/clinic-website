package com.healthcare.clinic.ecommerce.service;

import com.healthcare.clinic.ecommerce.entity.EcShipment;
import com.healthcare.clinic.ecommerce.entity.EcShipmentEvent;
import com.healthcare.clinic.ecommerce.entity.EcommerceOrder;
import com.healthcare.clinic.ecommerce.repository.EcShipmentEventRepository;
import com.healthcare.clinic.ecommerce.repository.EcShipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final EcShipmentRepository shipmentRepository;
    private final EcShipmentEventRepository shipmentEventRepository;
    private final OrderService orderService;

    @Transactional
    public EcShipment createShipment(Long orderId) {
        EcommerceOrder order = orderService.getOrderDetails(orderId, orderId); // Bypass sec for internal
        
        EcShipment shipment = EcShipment.builder()
                .orderId(orderId)
                .deliveryAddressId(order.getAddressId())
                .status("READY")
                .trackingNumber("TRK-" + UUID.randomUUID().toString().substring(0, 10).toUpperCase())
                .otpRequired(true)
                .build();
                
        return shipmentRepository.save(shipment);
    }

    @Transactional
    public void dispatchShipment(Long shipmentId, Long deliveryAgentId, String carrier) {
        EcShipment shipment = shipmentRepository.findById(shipmentId).orElseThrow();
        shipment.setAssignedTo(deliveryAgentId);
        shipment.setCarrier(carrier);
        shipment.setStatus("OUT_FOR_DELIVERY");
        shipment.setOutForDeliveryAt(ZonedDateTime.now());
        shipmentRepository.save(shipment);

        logEvent(shipmentId, "DISPATCHED", "Out for delivery with " + carrier);
        orderService.updateOrderStatus(shipment.getOrderId(), "DISPATCHED", deliveryAgentId, "DELIVERY_AGENT", "Dispatched");
    }

    @Transactional
    public void markDelivered(Long shipmentId, Long deliveryAgentId, String proofUrl, String otp) {
        EcShipment shipment = shipmentRepository.findById(shipmentId).orElseThrow();
        
        if (shipment.getOtpRequired() && (otp == null || !otp.equals("1234"))) { // Mock OTP validation
            throw new IllegalArgumentException("Invalid OTP for delivery");
        }

        shipment.setStatus("DELIVERED");
        shipment.setDeliveredAt(ZonedDateTime.now());
        shipment.setProofOfDeliveryUrl(proofUrl);
        shipment.setOtpVerified(true);
        shipmentRepository.save(shipment);

        logEvent(shipmentId, "DELIVERED", "Delivered successfully");
        orderService.updateOrderStatus(shipment.getOrderId(), "DELIVERED", deliveryAgentId, "DELIVERY_AGENT", "Delivered");
    }
    
    private void logEvent(Long shipmentId, String type, String note) {
        shipmentEventRepository.save(EcShipmentEvent.builder()
                .shipmentId(shipmentId)
                .eventType(type)
                .notes(note)
                .build());
    }
}
