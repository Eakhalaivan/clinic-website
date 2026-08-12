package com.healthcare.clinic.ecommerce.controller;

import com.healthcare.clinic.ecommerce.entity.EcommerceProduct;
import com.healthcare.clinic.ecommerce.service.ProductCatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ecommerce/products")
@RequiredArgsConstructor
public class EcommerceController {

    private final ProductCatalogService catalogService;

    @GetMapping
    public ResponseEntity<List<EcommerceProduct>> getActiveProducts() {
        return ResponseEntity.ok(catalogService.getActiveProducts());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<EcommerceProduct> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(catalogService.getProductDetails(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('PHARMACIST')")
    public ResponseEntity<EcommerceProduct> createOrUpdateProduct(@RequestBody EcommerceProduct product) {
        return ResponseEntity.ok(catalogService.createOrUpdateProduct(product));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> archiveProduct(@PathVariable Long id) {
        catalogService.archiveProduct(id);
        return ResponseEntity.ok().build();
    }
}
