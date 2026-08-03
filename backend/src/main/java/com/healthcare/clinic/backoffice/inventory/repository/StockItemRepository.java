package com.healthcare.clinic.backoffice.inventory.repository;

import com.healthcare.clinic.backoffice.inventory.entity.StockItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockItemRepository extends JpaRepository<StockItem, Long> {
    List<StockItem> findByWarehouseId(Long warehouseId);
    List<StockItem> findByItemType(String itemType);
    Optional<StockItem> findByMedicineBatchId(Long medicineBatchId);
}
