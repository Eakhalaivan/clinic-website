package com.healthcare.clinic.ecommerce.controller;

import com.healthcare.clinic.ecommerce.entity.EcommerceOrder;
import com.healthcare.clinic.ecommerce.service.OrderService;
import com.healthcare.clinic.identity.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ecommerce/orders")
@RequiredArgsConstructor
public class EcOrderController {

    private final OrderService orderService;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_PATIENT')")
    public ResponseEntity<List<EcommerceOrder>> getMyOrders(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(orderService.getPatientOrders(user.getId()));
    }

    @GetMapping("/{orderId}")
    @PreAuthorize("hasAuthority('ROLE_PATIENT')")
    public ResponseEntity<EcommerceOrder> getOrderDetails(
            @AuthenticationPrincipal User user,
            @PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderDetails(orderId, user.getId()));
    }

    @PostMapping("/{orderId}/status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('PHARMACIST')")
    public ResponseEntity<Void> updateOrderStatus(
            @AuthenticationPrincipal User user,
            @PathVariable Long orderId,
            @RequestParam String status,
            @RequestParam(required = false) String note) {
        orderService.updateOrderStatus(orderId, status, user.getId(), user.getRoles().stream().findFirst().map(com.healthcare.clinic.identity.entity.Role::getName).orElse("USER"), note);
        return ResponseEntity.ok().build();
    }
}
