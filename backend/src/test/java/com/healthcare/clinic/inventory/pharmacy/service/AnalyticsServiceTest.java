package com.healthcare.clinic.inventory.pharmacy.service;

import com.healthcare.clinic.inventory.pharmacy.dto.analytics.AnalyticsDashboardDTO;
import com.healthcare.clinic.inventory.pharmacy.repository.MedicineRepository;
import com.healthcare.clinic.inventory.pharmacy.repository.MedicineStockRepository;
import com.healthcare.clinic.inventory.sales.repository.MedicineReturnRepository;
import com.healthcare.clinic.inventory.sales.repository.PharmacyBillRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnalyticsServiceTest {

    @Mock
    private PharmacyBillRepository billRepository;
    @Mock
    private MedicineStockRepository stockRepository;
    @Mock
    private MedicineRepository medicineRepository;
    @Mock
    private MedicineReturnRepository returnRepository;
    @Mock
    private EntityManager entityManager;

    @Mock
    private Query mockQuery;

    @InjectMocks
    private AnalyticsService analyticsService;

    private LocalDateTime start;
    private LocalDateTime end;

    @BeforeEach
    void setUp() {
        start = LocalDateTime.now().minusDays(7);
        end = LocalDateTime.now();
    }

    @Test
    void testGetDashboard_NormalDataPath() {
        when(billRepository.sumNetAmountByBillingDateBetween(any(), any())).thenReturn(new BigDecimal("1000.00"));
        when(returnRepository.sumTotalReturnAmountByDateAndStatus(any(), any(), any())).thenReturn(new BigDecimal("100.00"));
        when(entityManager.createNativeQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.setParameter(anyString(), any())).thenReturn(mockQuery);
        when(mockQuery.getSingleResult()).thenReturn(new BigDecimal("500.00"));

        AnalyticsDashboardDTO dashboard = analyticsService.getDashboardSummary(start, end);

        assertNotNull(dashboard);
        assertEquals(new BigDecimal("1000.00"), dashboard.getTotalSalesRevenue().getCurrentValue());
        assertEquals(new BigDecimal("900.00"), dashboard.getNetRevenue().getCurrentValue()); // 1000 - 100
        assertEquals(new BigDecimal("500.00"), dashboard.getTotalPurchases().getCurrentValue());
    }

    @Test
    void testGetDashboard_EmptyDb_NoDataYet() {
        when(billRepository.sumNetAmountByBillingDateBetween(any(), any())).thenReturn(null);
        when(entityManager.createNativeQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.setParameter(anyString(), any())).thenReturn(mockQuery);
        // Simulate an empty db where native query might throw NoResultException
        when(mockQuery.getSingleResult()).thenThrow(new NoResultException("No result found"));

        AnalyticsDashboardDTO dashboard = analyticsService.getDashboardSummary(start, end);

        assertNotNull(dashboard);
        assertEquals(BigDecimal.ZERO, dashboard.getTotalSalesRevenue().getCurrentValue());
        assertEquals(BigDecimal.ZERO, dashboard.getTotalPurchases().getCurrentValue());
    }

    @Test
    void testGetDashboard_SimulatedException_ShouldCatchAndReturnZeroed() {
        when(billRepository.sumNetAmountByBillingDateBetween(any(), any())).thenThrow(new RuntimeException("DB Connection failed"));

        AnalyticsDashboardDTO dashboard = analyticsService.getDashboardSummary(start, end);

        assertNotNull(dashboard);
        assertEquals(BigDecimal.ZERO, dashboard.getTotalSalesRevenue().getCurrentValue());
        assertEquals(BigDecimal.ZERO, dashboard.getTotalPurchases().getCurrentValue());
    }
}
