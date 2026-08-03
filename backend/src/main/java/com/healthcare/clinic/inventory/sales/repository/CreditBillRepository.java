package com.healthcare.clinic.inventory.sales.repository;

import com.healthcare.clinic.inventory.sales.model.CreditBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("pharmacyCreditBillRepository")
public interface CreditBillRepository extends JpaRepository<CreditBill, Long> {
    Optional<CreditBill> findByBillId(Long billId);
    long countByStatus(com.healthcare.clinic.inventory.pharmacy.enums.PaymentStatus status);
}
