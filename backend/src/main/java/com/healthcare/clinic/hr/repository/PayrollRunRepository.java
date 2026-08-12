package com.healthcare.clinic.hr.repository;

import com.healthcare.clinic.hr.entity.PayrollRun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PayrollRunRepository extends JpaRepository<PayrollRun, Long> {
}
