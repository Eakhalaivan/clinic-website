package com.healthcare.clinic.ecommerce.controller;

import com.healthcare.clinic.ecommerce.entity.EcommerceOrder;
import com.healthcare.clinic.ecommerce.repository.EcommerceOrderRepository;
import com.healthcare.clinic.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/patient/orders")
@RequiredArgsConstructor
public class PatientOrdersController {

    private final EcommerceOrderRepository ecommerceOrderRepository;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_PATIENT')")
    public ResponseEntity<List<EcommerceOrder>> getMyOrders() {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        
        List<EcommerceOrder> orders = ecommerceOrderRepository.findByUserId(currentUserId);
        
        return ResponseEntity.ok(orders);
    }
}
