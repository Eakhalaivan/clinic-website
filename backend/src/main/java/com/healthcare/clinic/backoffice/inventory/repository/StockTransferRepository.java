package com.healthcare.clinic.backoffice.inventory.repository;

import com.healthcare.clinic.backoffice.inventory.entity.StockTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockTransferRepository extends JpaRepository<StockTransfer, Long> {
    List<StockTransfer> findAllByOrderByTransferredAtDesc();
    List<StockTransfer> findByFromWarehouseIdOrToWarehouseIdOrderByTransferredAtDesc(Long fromId, Long toId);
}
