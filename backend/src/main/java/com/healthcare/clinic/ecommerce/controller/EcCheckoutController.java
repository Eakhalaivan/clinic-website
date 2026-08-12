package com.healthcare.clinic.ecommerce.controller;

import com.healthcare.clinic.ecommerce.entity.EcPayment;
import com.healthcare.clinic.ecommerce.entity.EcommerceOrder;
import com.healthcare.clinic.ecommerce.service.CheckoutService;
import com.healthcare.clinic.ecommerce.service.PaymentService;
import com.healthcare.clinic.identity.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ecommerce/checkout")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_PATIENT')")
public class EcCheckoutController {

    private final CheckoutService checkoutService;
    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<EcPayment> processCheckout(
            @AuthenticationPrincipal User user,
            @RequestParam Long cartId,
            @RequestParam Long addressId) {
        
        // 1. Convert Cart to Order (locks cart, calculates totals)
        EcommerceOrder order = checkoutService.processCheckout(user.getId(), cartId, addressId);
        
        // 2. Initiate Payment (generates idempotency key, provider ref)
        EcPayment payment = paymentService.initiatePayment(order);
        
        return ResponseEntity.ok(payment);
    }
}
