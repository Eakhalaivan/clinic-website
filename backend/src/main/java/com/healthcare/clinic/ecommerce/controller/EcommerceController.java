package com.healthcare.clinic.ecommerce.controller;

import com.healthcare.clinic.ecommerce.entity.EcommerceOrder;
import com.healthcare.clinic.ecommerce.entity.EcommerceProduct;
import com.healthcare.clinic.ecommerce.service.EcommerceService;
import com.healthcare.clinic.identity.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ecommerce")
@RequiredArgsConstructor
public class EcommerceController {

    private final EcommerceService ecommerceService;

    @GetMapping("/products")
    public ResponseEntity<List<EcommerceProduct>> getProducts() {
        return ResponseEntity.ok(ecommerceService.getAllProducts());
    }

    @PostMapping("/products")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('MARKETING') or hasRole('PHARMACIST')")
    public ResponseEntity<EcommerceProduct> createProduct(@RequestBody EcommerceProduct product) {
        return ResponseEntity.ok(ecommerceService.createProduct(product));
    }

    @GetMapping("/orders")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('MARKETING') or hasRole('PHARMACIST')")
    public ResponseEntity<List<EcommerceOrder>> getAllOrders() {
        return ResponseEntity.ok(ecommerceService.getAllOrders());
    }

    @GetMapping("/my-orders")
    public ResponseEntity<List<EcommerceOrder>> getMyOrders(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ecommerceService.getUserOrders(user.getId()));
    }

    @PatchMapping("/orders/{id}/shipping")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('PHARMACIST')")
    public ResponseEntity<EcommerceOrder> updateShipping(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam(required = false) String trackingNumber) {
        return ResponseEntity.ok(ecommerceService.updateShipping(id, status, trackingNumber));
    }
}
