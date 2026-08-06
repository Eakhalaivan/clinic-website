package com.healthcare.clinic.pharmacy.repository;

import com.healthcare.clinic.inventory.entity.BaseEntity;
import com.healthcare.clinic.inventory.entity.Patient;

import com.healthcare.clinic.pharmacy.entity.ReturnToSupplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("pharmacyReturnToSupplierRepository")
public interface ReturnToSupplierRepository extends JpaRepository<ReturnToSupplier, Long> {
    List<ReturnToSupplier> findBySupplierId(Long supplierId);
    List<ReturnToSupplier> findByGoodsReceiptNoteId(Long grnId);
}
