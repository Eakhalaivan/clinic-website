package com.healthcare.clinic.billing.service;

import com.healthcare.clinic.billing.entity.Invoice;
import com.healthcare.clinic.billing.entity.InvoiceItem;
import com.healthcare.clinic.billing.entity.InvoiceStatus;
import com.healthcare.clinic.billing.repository.InvoiceItemRepository;
import com.healthcare.clinic.billing.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.healthcare.clinic.finance.service.TaxCalculationService;

@Service
@RequiredArgsConstructor
@Slf4j
public class UnifiedBillingService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceItemRepository invoiceItemRepository;
    private final TaxCalculationService taxCalculationService;

    @Transactional
    public Invoice calculateTotals(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found: " + invoiceId));
        
        List<InvoiceItem> items = invoiceItemRepository.findByInvoiceId(invoiceId);
        
        BigDecimal subtotal = items.stream()
                .map(InvoiceItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        invoice.setAmount(subtotal);
        
        BigDecimal tax = taxCalculationService.calculateTaxForAmount(subtotal);
        invoice.setTaxAmount(tax);
        
        BigDecimal discount = invoice.getDiscountAmount() != null ? invoice.getDiscountAmount() : BigDecimal.ZERO;
        
        BigDecimal total = subtotal.add(tax).subtract(discount);
        
        if (total.compareTo(BigDecimal.ZERO) < 0) {
            total = BigDecimal.ZERO;
        }
        
        invoice.setTotalAmount(total);
        
        BigDecimal patientResp = total.subtract(invoice.getInsuranceCoverage());
        if (patientResp.compareTo(BigDecimal.ZERO) < 0) {
            patientResp = BigDecimal.ZERO;
        }
        
        invoice.setPatientResponsibility(patientResp);
        invoice.setOutstandingBalance(patientResp.subtract(invoice.getAmountPaid()));
        
        return invoiceRepository.save(invoice);
    }
    
    @Transactional
    public Invoice issueInvoice(Long invoiceId) {
        Invoice invoice = calculateTotals(invoiceId);
        
        if (invoice.getStatus() != InvoiceStatus.DRAFT) {
            throw new IllegalStateException("Only DRAFT invoices can be issued");
        }
        
        invoice.setStatus(InvoiceStatus.ISSUED);
        return invoiceRepository.save(invoice);
    }
    
    @Transactional
    public void recordPayment(Long invoiceId, BigDecimal amount) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found: " + invoiceId));
                
        BigDecimal newPaid = invoice.getAmountPaid().add(amount);
        invoice.setAmountPaid(newPaid);
        invoice.setOutstandingBalance(invoice.getPatientResponsibility().subtract(newPaid));
        
        if (invoice.getOutstandingBalance().compareTo(BigDecimal.ZERO) <= 0) {
            invoice.setStatus(InvoiceStatus.PAID);
            invoice.setOutstandingBalance(BigDecimal.ZERO);
        } else {
            invoice.setStatus(InvoiceStatus.PARTIALLY_PAID);
        }
        
        invoiceRepository.save(invoice);
    }
}
