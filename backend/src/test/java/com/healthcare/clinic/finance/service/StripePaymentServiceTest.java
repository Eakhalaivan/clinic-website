package com.healthcare.clinic.finance.service;

import com.healthcare.clinic.billing.entity.Invoice;
import com.healthcare.clinic.billing.entity.InvoiceStatus;
import com.healthcare.clinic.billing.repository.InvoiceRepository;
import com.healthcare.clinic.finance.repository.PaymentRepository;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StripePaymentServiceTest {

    @Mock
    private InvoiceRepository invoiceRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private StripePaymentService stripePaymentService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(stripePaymentService, "stripeSecretKey", "sk_test_123");
        ReflectionTestUtils.setField(stripePaymentService, "endpointSecret", "whsec_123");
        stripePaymentService.init();
    }

    @Test
    void testCreateCheckoutSession_Success() throws Exception {
        Invoice invoice = new Invoice();
        invoice.setId(1L);
        invoice.setInvoiceNumber("INV-1001");
        invoice.setTotalAmount(new BigDecimal("150.00"));
        invoice.setStatus(InvoiceStatus.PENDING);

        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));

        Session mockSession = mock(Session.class);
        when(mockSession.getUrl()).thenReturn("https://checkout.stripe.com/c/pay/cs_test_123");

        try (MockedStatic<Session> mockedSession = mockStatic(Session.class)) {
            mockedSession.when(() -> Session.create(any(SessionCreateParams.class))).thenReturn(mockSession);
            
            String url = stripePaymentService.createCheckoutSession(1L);
            assertEquals("https://checkout.stripe.com/c/pay/cs_test_123", url);
        }
    }

    @Test
    void testCreateCheckoutSession_AlreadyPaid() {
        Invoice invoice = new Invoice();
        invoice.setId(1L);
        invoice.setStatus(InvoiceStatus.PAID);

        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));

        assertThrows(ResponseStatusException.class, () -> {
            stripePaymentService.createCheckoutSession(1L);
        });
    }
}
