package com.healthcare.clinic.ecommerce.service;

import com.healthcare.clinic.ecommerce.entity.*;
import com.healthcare.clinic.ecommerce.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CheckoutService {

    private final CartService cartService;
    private final ShippingService shippingService;
    private final TaxService taxService;
    private final AddressService addressService;
    private final PaymentService paymentService;
    private final EcommerceOrderRepository orderRepository;
    private final EcommerceOrderItemRepository orderItemRepository;
    private final EcDeliveryAddressRepository addressRepository;

    @Transactional
    public EcommerceOrder processCheckout(Long patientId, Long cartId, Long addressId) {
        EcCart cart = cartService.getOrCreateCart(patientId, null);
        if (!cart.getId().equals(cartId) || cart.getItems().isEmpty()) {
            throw new IllegalStateException("Cart is empty or invalid");
        }

        EcDeliveryAddress address = addressRepository.findById(addressId).orElseThrow();
        if (!address.getPatientId().equals(patientId) || !address.getIsServiceable()) {
            throw new IllegalStateException("Invalid or unserviceable address");
        }

        EcDeliveryZone zone = shippingService.getDeliveryZone(address.getPincode());
        
        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal totalTax = BigDecimal.ZERO;
        
        EcommerceOrder order = EcommerceOrder.builder()
                .patientId(patientId)
                .orderNumber("ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .status("PENDING")
                .paymentStatus("PENDING")
                .addressId(addressId)
                .userId(patientId)
                .shippingAddress("TBD")
                .shippingCity("TBD")
                .postalCode("TBD")
                // Initialize totals to zero, calculate below
                .totalAmount(BigDecimal.ZERO) 
                .build();
                
        order = orderRepository.save(order);

        for (EcCartItem item : cart.getItems()) {
            EcommerceProduct p = item.getCart().getItems().get(0).getCart().getItems().isEmpty() ? null : null; // Hack to satisfy compiler for missing direct relation in this stub
            
            // Simplified tax calc
            EcTaxRule rule = taxService.getApplicableTaxRule("STANDARD", address.getState());
            BigDecimal itemTotal = item.getPriceSnapshot().multiply(BigDecimal.valueOf(item.getQuantity()));
            BigDecimal itemTax = taxService.calculateTax(itemTotal, rule);
            
            subtotal = subtotal.add(itemTotal);
            totalTax = totalTax.add(itemTax);

            orderItemRepository.save(EcommerceOrderItem.builder()
                    .order(order)
                    .product(EcommerceProduct.builder().id(item.getProductId()).build()) // proxy
                    .quantity(item.getQuantity())
                    .unitPrice(item.getPriceSnapshot())
                    .totalPrice(itemTotal)
                    .taxAmount(itemTax)
                    .prescriptionRequired(item.getPrescriptionId() != null)
                    .build());
        }

        BigDecimal shippingFee = shippingService.calculateShippingFee(zone, subtotal);
        BigDecimal grandTotal = subtotal.add(totalTax).add(shippingFee);

        order.setTotalAmount(grandTotal);
        orderRepository.save(order);

        // Lock cart
        cart.setStatus("CHECKED_OUT");
        
        return order;
    }
}
