package com.healthcare.clinic.marketing.repository;

import com.healthcare.clinic.marketing.entity.LoyaltyTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoyaltyTransactionRepository extends JpaRepository<LoyaltyTransaction, Long> {
    Page<LoyaltyTransaction> findByPatientIdOrderByCreatedAtDesc(Long patientId, Pageable pageable);
    Optional<LoyaltyTransaction> findByIdempotencyKey(String idempotencyKey);
}
