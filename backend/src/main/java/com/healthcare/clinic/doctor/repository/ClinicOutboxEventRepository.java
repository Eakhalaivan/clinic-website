package com.healthcare.clinic.doctor.repository;

import com.healthcare.clinic.doctor.entity.ClinicOutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ClinicOutboxEventRepository extends JpaRepository<ClinicOutboxEvent, Long> {
    List<ClinicOutboxEvent> findByStatus(String status);
}
