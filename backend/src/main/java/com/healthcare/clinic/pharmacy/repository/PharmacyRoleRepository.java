package com.healthcare.clinic.pharmacy.repository;

import com.healthcare.clinic.pharmacy.entity.PharmacyRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("pharmacyRoleRepository")
public interface PharmacyRoleRepository extends JpaRepository<PharmacyRole, Long> {
    Optional<PharmacyRole> findByName(String name);
}
