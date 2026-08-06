package com.healthcare.clinic.pharmacy.repository;

import com.healthcare.clinic.inventory.entity.BaseEntity;
import com.healthcare.clinic.inventory.entity.Patient;

import com.healthcare.clinic.pharmacy.entity.GoodsReceiptNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("pharmacyGoodsReceiptNoteRepository")
public interface GoodsReceiptNoteRepository extends JpaRepository<GoodsReceiptNote, Long> {
    Optional<GoodsReceiptNote> findByGrnNumber(String grnNumber);
    List<GoodsReceiptNote> findByPurchaseOrderPoId(String poId);
    List<GoodsReceiptNote> findBySupplierId(Long supplierId);

}
