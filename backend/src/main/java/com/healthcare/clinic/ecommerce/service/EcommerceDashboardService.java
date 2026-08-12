package com.healthcare.clinic.ecommerce.service;

import com.healthcare.clinic.ecommerce.entity.EcommerceOrder;
import com.healthcare.clinic.ecommerce.repository.EcommerceOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EcommerceDashboardService {

    private final EcommerceOrderRepository orderRepository;

    @Transactional(readOnly = true)
    public Map<String, Object> getAdminDashboardStats() {
        ZonedDateTime thirtyDaysAgo = ZonedDateTime.now().minusDays(30);
        
        List<EcommerceOrder> recentOrders = orderRepository.findAll().stream()
                .filter(o -> o.getCreatedAt().isAfter(thirtyDaysAgo))
                .toList();

        long totalOrders = recentOrders.size();
        
        BigDecimal revenue = recentOrders.stream()
                .filter(o -> "PAID".equals(o.getPaymentStatus()))
                .map(EcommerceOrder::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
                
        long pendingFulfillment = recentOrders.stream()
                .filter(o -> "PENDING".equals(o.getFulfillmentStatus()) && "CONFIRMED".equals(o.getStatus()))
                .count();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalOrders30d", totalOrders);
        stats.put("revenue30d", revenue);
        stats.put("pendingFulfillment", pendingFulfillment);
        
        return stats;
    }
}
