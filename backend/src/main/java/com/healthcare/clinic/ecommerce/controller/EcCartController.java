package com.healthcare.clinic.ecommerce.controller;

import com.healthcare.clinic.ecommerce.entity.EcCart;
import com.healthcare.clinic.ecommerce.service.CartService;
import com.healthcare.clinic.identity.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ecommerce/cart")
@RequiredArgsConstructor
public class EcCartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<EcCart> getCart(
            @AuthenticationPrincipal User user,
            @RequestHeader(value = "X-Session-Key", required = false) String sessionKey) {
        Long patientId = user != null ? user.getId() : null;
        return ResponseEntity.ok(cartService.getOrCreateCart(patientId, sessionKey));
    }

    @PostMapping("/items")
    public ResponseEntity<EcCart> addItem(
            @RequestParam Long cartId,
            @RequestParam Long productId,
            @RequestParam Integer quantity) {
        return ResponseEntity.ok(cartService.addItemToCart(cartId, productId, quantity));
    }

    @PostMapping("/merge")
    @PreAuthorize("hasAuthority('ROLE_PATIENT')")
    public ResponseEntity<Void> mergeCart(
            @AuthenticationPrincipal User user,
            @RequestHeader("X-Session-Key") String sessionKey) {
        cartService.mergeSessionCart(user.getId(), sessionKey);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> clearCart(@PathVariable Long cartId) {
        cartService.clearCart(cartId);
        return ResponseEntity.ok().build();
    }
}
