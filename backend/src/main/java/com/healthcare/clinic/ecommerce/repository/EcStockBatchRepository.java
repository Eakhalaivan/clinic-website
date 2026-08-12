package com.healthcare.clinic.ecommerce.repository;

import com.healthcare.clinic.ecommerce.entity.EcStockBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EcStockBatchRepository extends JpaRepository<EcStockBatch, Long> {
}
