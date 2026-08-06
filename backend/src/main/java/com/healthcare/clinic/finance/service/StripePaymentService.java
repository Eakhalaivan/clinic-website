package com.healthcare.clinic.finance.service;

import com.healthcare.clinic.billing.entity.Invoice;
import com.healthcare.clinic.billing.entity.InvoiceStatus;
import com.healthcare.clinic.billing.repository.InvoiceRepository;
import com.healthcare.clinic.finance.entity.Payment;
import com.healthcare.clinic.finance.repository.PaymentRepository;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class StripePaymentService {

    private final InvoiceRepository invoiceRepository;
    private final PaymentRepository paymentRepository;

    @Value("${stripe.secret-key}")
    private String stripeSecretKey;

    @Value("${stripe.webhook-secret}")
    private String endpointSecret;

    @Value("${stripe.publishable-key}")
    private String stripePublishableKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;
    }

    public String createCheckoutSession(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invoice not found"));

        if (invoice.getStatus() == InvoiceStatus.PAID) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Invoice is already paid");
        }

        long amountInCents = invoice.getTotalAmount().multiply(new BigDecimal("100")).longValue();

        try {
            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl("https://your-frontend-url.com/success?session_id={CHECKOUT_SESSION_ID}") // You should configure this via properties
                    .setCancelUrl("https://your-frontend-url.com/cancel")
                    .setClientReferenceId(invoice.getId().toString())
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setQuantity(1L)
                                    .setPriceData(
                                            SessionCreateParams.LineItem.PriceData.builder()
                                                    .setCurrency("usd")
                                                    .setUnitAmount(amountInCents)
                                                    .setProductData(
                                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                    .setName("Invoice #" + invoice.getInvoiceNumber())
                                                                    .build()
                                                    )
                                                    .build()
                                    )
                                    .build()
                    )
                    .build();

            Session session = Session.create(params);
            
            log.info("Created Stripe Checkout session for Invoice {}: {}", invoiceId, session.getUrl());
            
            return session.getUrl();
        } catch (StripeException e) {
            log.error("Failed to create Stripe session", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create payment session");
        }
    }

    public void processWebhook(String payload, String signature) {
        Event event;
        try {
            event = Webhook.constructEvent(payload, signature, endpointSecret);
        } catch (SignatureVerificationException e) {
            log.warn("Invalid Stripe signature");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid signature");
        } catch (Exception e) {
            log.error("Webhook processing error", e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid payload");
        }

        if ("checkout.session.completed".equals(event.getType())) {
            Session session = (Session) event.getDataObjectDeserializer().getObject().orElse(null);
            if (session != null && session.getClientReferenceId() != null) {
                Long invoiceId = Long.parseLong(session.getClientReferenceId());
                Invoice invoice = invoiceRepository.findById(invoiceId).orElse(null);
                
                if (invoice != null && invoice.getStatus() != InvoiceStatus.PAID) {
                    invoice.setStatus(InvoiceStatus.PAID);
                    invoice.setPaidAt(LocalDateTime.now());
                    invoiceRepository.save(invoice);
                    
                    Payment payment = Payment.builder()
                            .invoice(invoice)
                            .amount(invoice.getTotalAmount())
                            .paymentMethod("STRIPE")
                            .transactionRef(session.getId())
                            .build();
                    paymentRepository.save(payment);
                    
                    log.info("Invoice {} successfully marked as PAID via Stripe webhook.", invoiceId);
                }
            }
        }
    }
}
