package com.healthcare.clinic.ecommerce.controller;

import com.healthcare.clinic.ecommerce.entity.EcDeliveryAddress;
import com.healthcare.clinic.ecommerce.service.AddressService;
import com.healthcare.clinic.identity.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ecommerce/addresses")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_PATIENT')")
public class EcAddressController {

    private final AddressService addressService;

    @GetMapping
    public ResponseEntity<List<EcDeliveryAddress>> getAddresses(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(addressService.getPatientAddresses(user.getId()));
    }

    @PostMapping
    public ResponseEntity<EcDeliveryAddress> saveAddress(
            @AuthenticationPrincipal User user,
            @RequestBody EcDeliveryAddress address) {
        address.setPatientId(user.getId());
        return ResponseEntity.ok(addressService.saveAddress(address));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@AuthenticationPrincipal User user, @PathVariable Long id) {
        addressService.deleteAddress(id, user.getId());
        return ResponseEntity.ok().build();
    }
}
