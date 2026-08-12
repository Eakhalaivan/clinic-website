package com.healthcare.clinic.reception.service;

import com.healthcare.clinic.reception.entity.ClinicBill;
import com.healthcare.clinic.reception.entity.ClinicBillItem;
import com.healthcare.clinic.reception.entity.ClinicPayment;
import com.healthcare.clinic.reception.repository.ClinicBillRepository;
import com.healthcare.clinic.reception.repository.ClinicPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ClinicBillingService {

    private final ClinicBillRepository billRepository;
    private final ClinicPaymentRepository paymentRepository;

    @Transactional
    public ClinicBill createBill(Long patientId, Long appointmentId, Long walkInId, List<Map<String, Object>> items) {
        ClinicBill bill = ClinicBill.builder()
                .patientId(patientId)
                .appointmentId(appointmentId)
                .walkInId(walkInId)
                .status("PENDING")
                .totalAmount(BigDecimal.ZERO)
                .discount(BigDecimal.ZERO)
                .netAmount(BigDecimal.ZERO)
                .build();

        BigDecimal total = BigDecimal.ZERO;
        
        for (Map<String, Object> itemData : items) {
            String description = (String) itemData.get("description");
            BigDecimal amount = new BigDecimal(itemData.get("amount").toString());
            String department = (String) itemData.get("department");

            ClinicBillItem item = ClinicBillItem.builder()
                    .bill(bill)
                    .description(description)
                    .amount(amount)
                    .department(department)
                    .build();
            
            bill.getItems().add(item);
            total = total.add(amount);
        }

        bill.setTotalAmount(total);
        bill.setNetAmount(total); // Can apply discount later

        return billRepository.save(bill);
    }

    @Transactional
    public ClinicPayment recordPayment(Long billId, BigDecimal amount, String paymentMethod, String referenceNumber) {
        ClinicBill bill = billRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found"));
        
        ClinicPayment payment = ClinicPayment.builder()
                .bill(bill)
                .amount(amount)
                .paymentMethod(paymentMethod)
                .referenceNumber(referenceNumber)
                .status("COMPLETED")
                .build();
        
        ClinicPayment savedPayment = paymentRepository.save(payment);
        
        // Update bill status if fully paid
        List<ClinicPayment> payments = paymentRepository.findByBillId(billId);
        BigDecimal totalPaid = payments.stream().map(ClinicPayment::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        
        if (totalPaid.compareTo(bill.getNetAmount()) >= 0) {
            bill.setStatus("PAID");
            billRepository.save(bill);
        }
        
        return savedPayment;
    }

    public List<ClinicBill> getBillsForPatient(Long patientId) {
        return billRepository.findByPatientId(patientId);
    }
}
