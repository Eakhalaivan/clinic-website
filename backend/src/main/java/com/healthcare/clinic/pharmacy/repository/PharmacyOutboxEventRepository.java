package com.healthcare.clinic.pharmacy.repository;

import com.healthcare.clinic.pharmacy.entity.PharmacyOutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PharmacyOutboxEventRepository extends JpaRepository<PharmacyOutboxEvent, Long> {
    List<PharmacyOutboxEvent> findByStatus(String status);
}
