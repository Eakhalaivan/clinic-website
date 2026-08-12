package com.healthcare.clinic.ecommerce.controller;

import com.healthcare.clinic.ecommerce.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ecommerce/payments/webhook")
@RequiredArgsConstructor
public class PaymentWebhookController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<String> handleWebhook(
            @RequestBody String payload,
            @RequestHeader("X-Signature") String signature) {
        
        paymentService.handlePaymentWebhook(payload, signature);
        return ResponseEntity.ok("OK");
    }
}
