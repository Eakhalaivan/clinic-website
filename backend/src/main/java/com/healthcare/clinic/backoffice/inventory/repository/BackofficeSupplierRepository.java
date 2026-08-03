package com.healthcare.clinic.backoffice.inventory.repository;

import com.healthcare.clinic.backoffice.inventory.entity.BackofficeSupplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BackofficeSupplierRepository extends JpaRepository<BackofficeSupplier, Long> {
    List<BackofficeSupplier> findByIsActiveTrue();
}
