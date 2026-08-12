package com.healthcare.clinic.support.repository;

import com.healthcare.clinic.support.entity.SpSlaPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpSlaPolicyRepository extends JpaRepository<SpSlaPolicy, Long> {
}
