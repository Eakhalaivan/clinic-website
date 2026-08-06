package com.healthcare.clinic.pharmacy.service;

import com.healthcare.clinic.inventory.entity.BaseEntity;
import com.healthcare.clinic.inventory.entity.Patient;

import com.healthcare.clinic.pharmacy.entity.*;
import com.healthcare.clinic.pharmacy.model.*;
import com.healthcare.clinic.pharmacy.repository.*;

import com.healthcare.clinic.pharmacy.model.CreditBill;
import com.healthcare.clinic.pharmacy.model.PharmacyBill;
import com.healthcare.clinic.pharmacy.enums.PaymentStatus;
import com.healthcare.clinic.pharmacy.exception.ResourceNotFoundException;
import com.healthcare.clinic.pharmacy.repository.CreditBillRepository;
import com.healthcare.clinic.pharmacy.repository.PharmacyBillRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service("pharmacyPaymentService")
public class PaymentService {

    private final PharmacyBillRepository pharmacyBillRepository;
    private final CreditBillRepository creditBillRepository;

    public PaymentService(PharmacyBillRepository pharmacyBillRepository, CreditBillRepository creditBillRepository) {
        this.pharmacyBillRepository = pharmacyBillRepository;
        this.creditBillRepository = creditBillRepository;
    }

    @Transactional
    public PharmacyBill applyPartialPayment(Long billId, BigDecimal amount) {
        PharmacyBill bill = pharmacyBillRepository.findById(billId)
                .orElseThrow(() -> new ResourceNotFoundException("Pharmacy bill not found with id: " + billId));

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Payment amount must be greater than zero");
        }

        BigDecimal newPaidAmount = bill.getPaidAmount().add(amount);
        if (newPaidAmount.compareTo(bill.getNetAmount()) > 0) {
            throw new IllegalArgumentException("Total paid amount cannot exceed net amount");
        }

        BigDecimal newBalance = bill.getNetAmount().subtract(newPaidAmount);
        
        bill.setPaidAmount(newPaidAmount);
        bill.setBalanceAmount(newBalance);

        if (newBalance.compareTo(BigDecimal.ZERO) <= 0) {
            bill.setPaymentStatus(PaymentStatus.PAID);
            bill.setStatus("PAID");
        } else {
            bill.setPaymentStatus(PaymentStatus.PARTIAL);
        }

        PharmacyBill savedBill = pharmacyBillRepository.save(bill);

        Optional<CreditBill> creditBillOpt = creditBillRepository.findByBillId(billId);
        if (creditBillOpt.isPresent()) {
            CreditBill creditBill = creditBillOpt.get();
            creditBill.setPaidAmount(newPaidAmount);
            creditBill.setBalanceAmount(newBalance);
            creditBill.setStatus(savedBill.getPaymentStatus());
            creditBillRepository.save(creditBill);
        }

        return savedBill;
    }
}
