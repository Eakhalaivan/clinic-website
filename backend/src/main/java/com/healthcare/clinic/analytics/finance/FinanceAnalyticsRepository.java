package com.healthcare.clinic.analytics.finance;

import com.healthcare.clinic.billing.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FinanceAnalyticsRepository extends JpaRepository<Invoice, Long> {

    @Query(value = "SELECT CAST(i.created_at AS DATE) as metricDate, SUM(i.total_amount) as totalRevenue " +
            "FROM invoices i " +
            "WHERE (:branchId IS NULL OR i.branch_id = :branchId) " +
            "AND i.status = 'PAID' " +
            "AND i.created_at >= :startDate AND i.created_at <= :endDate " +
            "GROUP BY CAST(i.created_at AS DATE) " +
            "ORDER BY metricDate ASC", nativeQuery = true)
    List<Object[]> getDailyRevenue(@Param("branchId") Long branchId,
                                   @Param("startDate") LocalDateTime startDate,
                                   @Param("endDate") LocalDateTime endDate);

    @Query(value = "SELECT i.payment_method as paymentMethod, SUM(i.total_amount) as totalRevenue " +
            "FROM invoices i " +
            "WHERE (:branchId IS NULL OR i.branch_id = :branchId) " +
            "AND i.status = 'PAID' " +
            "AND i.payment_method IS NOT NULL " +
            "AND i.created_at >= :startDate AND i.created_at <= :endDate " +
            "GROUP BY i.payment_method " +
            "ORDER BY totalRevenue DESC", nativeQuery = true)
    List<Object[]> getRevenueByPaymentMethod(@Param("branchId") Long branchId,
                                             @Param("startDate") LocalDateTime startDate,
                                             @Param("endDate") LocalDateTime endDate);

    @Query(value = "SELECT i.status as status, SUM(i.total_amount) as totalAmount " +
            "FROM invoices i " +
            "WHERE (:branchId IS NULL OR i.branch_id = :branchId) " +
            "AND i.created_at >= :startDate AND i.created_at <= :endDate " +
            "GROUP BY i.status", nativeQuery = true)
    List<Object[]> getInvoiceStatusSummary(@Param("branchId") Long branchId,
                                           @Param("startDate") LocalDateTime startDate,
                                           @Param("endDate") LocalDateTime endDate);
}
