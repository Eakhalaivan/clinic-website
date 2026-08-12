package com.healthcare.clinic.ecommerce.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthcare.clinic.ecommerce.entity.EcPayment;
import com.healthcare.clinic.ecommerce.entity.EcommerceOrder;
import com.healthcare.clinic.ecommerce.repository.EcPaymentRepository;
import com.healthcare.clinic.ecommerce.repository.EcommerceOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service("ecommercePaymentService")
@RequiredArgsConstructor
public class PaymentService {

    private final EcPaymentRepository paymentRepository;
    private final EcommerceOrderRepository orderRepository;
    private final ObjectMapper objectMapper;
    private final InventoryService inventoryService;

    @Value("${ecommerce.payment.provider:MOCK}")
    private String paymentProvider;

    @Transactional
    public EcPayment initiatePayment(EcommerceOrder order) {
        String idempotencyKey = "PAY_" + order.getId() + "_" + UUID.randomUUID().toString();
        
        EcPayment payment = EcPayment.builder()
                .orderId(order.getId())
                .provider(paymentProvider)
                .idempotencyKey(idempotencyKey)
                .amount(order.getTotalAmount())
                .currency("INR")
                .status("INITIATED")
                .build();

        if ("MOCK".equals(paymentProvider)) {
            payment.setProviderRef("MOCK_TXN_" + UUID.randomUUID().toString().substring(0, 8));
            log.info("Mock payment initiated for order {}, ref {}", order.getId(), payment.getProviderRef());
        } else {
            // Integration with Razorpay/Stripe would go here. We generate a provider reference in advance for mock.
            // e.g. RazorpayClient.Orders.create(...)
            throw new UnsupportedOperationException("Real payment gateways not implemented yet in Phase 17");
        }

        return paymentRepository.save(payment);
    }

    @Transactional
    public void handlePaymentWebhook(String payload, String signature) {
        // Idempotent webhook handler
        try {
            // Very naive mock verification, in reality verify signature here.
            Map<String, Object> data = objectMapper.readValue(payload, Map.class);
            String providerRef = (String) data.get("providerRef");
            String status = (String) data.get("status");

            EcPayment payment = paymentRepository.findAll().stream()
                    .filter(p -> providerRef.equals(p.getProviderRef()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Unknown payment reference"));

            if ("CAPTURED".equals(payment.getStatus()) || "FAILED".equals(payment.getStatus())) {
                log.info("Payment {} already processed. Idempotent return.", payment.getId());
                return; // Already processed
            }

            payment.setWebhookVerified(true);
            payment.setPgResponse(payload);

            EcommerceOrder order = orderRepository.findById(payment.getOrderId()).orElseThrow();

            if ("SUCCESS".equalsIgnoreCase(status)) {
                payment.setStatus("CAPTURED");
                payment.setCapturedAt(ZonedDateTime.now());
                order.setPaymentStatus("PAID");
                order.setStatus("CONFIRMED");
                
                // Convert reservation to actual sale
                inventoryService.convertReservationToSale(order.getPatientId(), order.getId()); // Using patientId as pseudo-cartId for now if Cart was cleared
            } else {
                payment.setStatus("FAILED");
                payment.setFailedAt(ZonedDateTime.now());
                order.setPaymentStatus("FAILED");
                order.setStatus("CANCELLED");
            }
            
            paymentRepository.save(payment);
            orderRepository.save(order);

        } catch (Exception e) {
            log.error("Failed to process payment webhook", e);
            throw new RuntimeException("Webhook processing failed", e);
        }
    }
}
