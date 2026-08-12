package com.healthcare.clinic.support.repository;

import com.healthcare.clinic.support.entity.SpEscalation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpEscalationRepository extends JpaRepository<SpEscalation, Long> {
}
