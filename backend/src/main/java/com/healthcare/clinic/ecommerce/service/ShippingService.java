package com.healthcare.clinic.ecommerce.service;

import com.healthcare.clinic.ecommerce.entity.EcDeliveryZone;
import com.healthcare.clinic.ecommerce.repository.EcDeliveryZoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ShippingService {

    private final EcDeliveryZoneRepository deliveryZoneRepository;

    @Transactional(readOnly = true)
    public EcDeliveryZone getDeliveryZone(String pincode) {
        return deliveryZoneRepository.findAll().stream()
                .filter(z -> z.getPincode().equals(pincode))
                .findFirst()
                .orElse(null);
    }

    public BigDecimal calculateShippingFee(EcDeliveryZone zone, BigDecimal cartSubtotal) {
        if (zone == null || !zone.getIsServiceable()) {
            throw new IllegalStateException("Delivery is not serviceable at the given pincode");
        }
        
        if (zone.getFreeShippingAbove() != null && cartSubtotal.compareTo(zone.getFreeShippingAbove()) >= 0) {
            return BigDecimal.ZERO;
        }
        
        return zone.getBaseShippingFee();
    }
}
