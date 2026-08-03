package com.healthcare.clinic.ecommerce.service;

import com.healthcare.clinic.ecommerce.entity.EcommerceOrder;
import com.healthcare.clinic.ecommerce.entity.EcommerceOrderItem;
import com.healthcare.clinic.ecommerce.entity.EcommerceProduct;
import com.healthcare.clinic.ecommerce.repository.EcommerceOrderItemRepository;
import com.healthcare.clinic.ecommerce.repository.EcommerceOrderRepository;
import com.healthcare.clinic.ecommerce.repository.EcommerceProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EcommerceService {

    private final EcommerceProductRepository productRepository;
    private final EcommerceOrderRepository orderRepository;
    private final EcommerceOrderItemRepository orderItemRepository;

    @Transactional(readOnly = true)
    public List<EcommerceProduct> getAllProducts() {
        return productRepository.findByIsActiveTrue();
    }

    @Transactional
    public EcommerceProduct createProduct(EcommerceProduct product) {
        return productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public List<EcommerceOrder> getAllOrders() {
        return orderRepository.findAllByOrderByCreatedAtDesc();
    }

    @Transactional(readOnly = true)
    public List<EcommerceOrder> getUserOrders(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Transactional
    public EcommerceOrder placeOrder(Long userId, String address, String city, String postalCode, List<EcommerceOrderItem> items) {
        BigDecimal total = BigDecimal.ZERO;

        for (EcommerceOrderItem item : items) {
            EcommerceProduct product = productRepository.findById(item.getProduct().getId()).orElseThrow();
            if (product.getStockQuantity() < item.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + product.getTitle());
            }
            product.setStockQuantity(product.getStockQuantity() - item.getQuantity());
            productRepository.save(product);

            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            item.setProduct(product);
            item.setUnitPrice(product.getPrice());
            item.setTotalPrice(itemTotal);
            total = total.add(itemTotal);
        }

        EcommerceOrder order = EcommerceOrder.builder()
                .userId(userId)
                .shippingAddress(address)
                .shippingCity(city)
                .postalCode(postalCode)
                .totalAmount(total)
                .status("PROCESSING")
                .build();

        for (EcommerceOrderItem item : items) {
            item.setOrder(order);
        }
        order.setItems(items);

        return orderRepository.save(order);
    }

    @Transactional
    public EcommerceOrder updateShipping(Long orderId, String status, String trackingNumber) {
        EcommerceOrder order = orderRepository.findById(orderId).orElseThrow();
        order.setStatus(status);
        if (trackingNumber != null && !trackingNumber.isEmpty()) {
            order.setTrackingNumber(trackingNumber);
        }
        if ("SHIPPED".equals(status)) {
            order.setShippedAt(ZonedDateTime.now());
        }
        return orderRepository.save(order);
    }
}
