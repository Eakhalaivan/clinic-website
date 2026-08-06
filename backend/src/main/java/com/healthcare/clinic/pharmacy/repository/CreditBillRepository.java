package com.healthcare.clinic.pharmacy.repository;

import com.healthcare.clinic.inventory.entity.BaseEntity;
import com.healthcare.clinic.inventory.entity.Patient;

import com.healthcare.clinic.pharmacy.model.CreditBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("pharmacyCreditBillRepository")
public interface CreditBillRepository extends JpaRepository<CreditBill, Long> {
    Optional<CreditBill> findByBillId(Long billId);
    long countByStatus(com.healthcare.clinic.pharmacy.enums.PaymentStatus status);
}
