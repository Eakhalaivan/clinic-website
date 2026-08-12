package com.healthcare.clinic.doctor.repository;

import com.healthcare.clinic.doctor.entity.BillingOutbox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillingOutboxRepository extends JpaRepository<BillingOutbox, Long> {
    List<BillingOutbox> findByStatus(String status);
    List<BillingOutbox> findByEncounterId(Long encounterId);
}
