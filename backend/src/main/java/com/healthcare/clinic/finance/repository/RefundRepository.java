package com.healthcare.clinic.finance.repository;

import com.healthcare.clinic.finance.entity.Refund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface RefundRepository extends JpaRepository<Refund, Long> {
    Optional<Refund> findByRefundReference(String refundReference);
    List<Refund> findByOriginalPaymentId(Long paymentId);
}
