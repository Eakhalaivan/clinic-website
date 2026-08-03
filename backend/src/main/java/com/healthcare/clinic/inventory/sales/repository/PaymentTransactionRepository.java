package com.healthcare.clinic.inventory.sales.repository;

import com.healthcare.clinic.inventory.sales.model.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("pharmacyPaymentTransactionRepository")
public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Long> {
    List<PaymentTransaction> findByCreditBillId(Long creditBillId);
}
