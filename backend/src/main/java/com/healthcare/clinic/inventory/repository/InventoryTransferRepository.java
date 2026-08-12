package com.healthcare.clinic.inventory.repository;

import com.healthcare.clinic.inventory.entity.InventoryTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryTransferRepository extends JpaRepository<InventoryTransfer, Long> {
}
