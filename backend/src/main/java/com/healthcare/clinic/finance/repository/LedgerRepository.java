package com.healthcare.clinic.finance.repository;

import com.healthcare.clinic.finance.entity.LedgerEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface LedgerRepository extends JpaRepository<LedgerEntry, Long> {

    @Query("SELECT l FROM LedgerEntry l WHERE l.branchId = :branchId AND l.entryDate >= :startDate AND l.entryDate <= :endDate")
    List<LedgerEntry> findByBranchIdAndDateRange(
            @Param("branchId") Long branchId, 
            @Param("startDate") LocalDate startDate, 
            @Param("endDate") LocalDate endDate);
            
    @Query("SELECT l FROM LedgerEntry l WHERE l.entryDate >= :startDate AND l.entryDate <= :endDate")
    List<LedgerEntry> findAllByDateRange(
            @Param("startDate") LocalDate startDate, 
            @Param("endDate") LocalDate endDate);
}
