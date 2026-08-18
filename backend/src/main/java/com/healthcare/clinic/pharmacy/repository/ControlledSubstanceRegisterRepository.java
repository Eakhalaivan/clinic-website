package com.healthcare.clinic.pharmacy.repository;

import com.healthcare.clinic.pharmacy.entity.ControlledSubstanceRegister;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ControlledSubstanceRegisterRepository extends JpaRepository<ControlledSubstanceRegister, Long> {
}
