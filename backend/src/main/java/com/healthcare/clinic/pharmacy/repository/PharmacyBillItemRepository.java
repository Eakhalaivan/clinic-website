package com.healthcare.clinic.pharmacy.repository;

import com.healthcare.clinic.inventory.entity.BaseEntity;
import com.healthcare.clinic.inventory.entity.Patient;

import com.healthcare.clinic.pharmacy.model.PharmacyBillItem;
import com.healthcare.clinic.pharmacy.dto.reports.MedicineWiseSalesSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PharmacyBillItemRepository extends JpaRepository<PharmacyBillItem, Long> {

    @Query("""
        SELECT 
            m.name AS medicine, 
            SUM(i.quantity) AS unitsSold, 
            SUM(i.netAmount) AS revenue, 
            SUM(i.taxAmount) AS tax 
        FROM PharmacyBillItem i 
        JOIN i.bill b 
        JOIN i.stock s 
        JOIN s.medicine m 
        WHERE b.billingDate BETWEEN :from AND :to 
          AND b.deleted = false 
        GROUP BY m.name 
        ORDER BY revenue DESC
    """)
    List<MedicineWiseSalesSummary> getMedicineWiseSales(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);
}
