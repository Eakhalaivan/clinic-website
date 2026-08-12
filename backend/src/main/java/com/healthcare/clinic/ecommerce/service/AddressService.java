package com.healthcare.clinic.ecommerce.service;

import com.healthcare.clinic.ecommerce.entity.EcDeliveryAddress;
import com.healthcare.clinic.ecommerce.entity.EcDeliveryZone;
import com.healthcare.clinic.ecommerce.repository.EcDeliveryAddressRepository;
import com.healthcare.clinic.ecommerce.repository.EcDeliveryZoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final EcDeliveryAddressRepository addressRepository;
    private final EcDeliveryZoneRepository zoneRepository;

    @Transactional(readOnly = true)
    public List<EcDeliveryAddress> getPatientAddresses(Long patientId) {
        return addressRepository.findAll().stream()
                .filter(a -> patientId.equals(a.getPatientId()) && !a.getIsDeleted())
                .toList();
    }

    @Transactional
    public EcDeliveryAddress saveAddress(EcDeliveryAddress address) {
        // Validate serviceability based on pincode
        Optional<EcDeliveryZone> zone = zoneRepository.findAll().stream()
                .filter(z -> z.getPincode().equals(address.getPincode()))
                .findFirst();

        address.setIsServiceable(zone.map(EcDeliveryZone::getIsServiceable).orElse(false));

        if (address.getIsDefault()) {
            // Unset other defaults for this patient
            getPatientAddresses(address.getPatientId()).stream()
                    .filter(EcDeliveryAddress::getIsDefault)
                    .forEach(a -> {
                        a.setIsDefault(false);
                        addressRepository.save(a);
                    });
        }
        return addressRepository.save(address);
    }

    @Transactional
    public void deleteAddress(Long id, Long patientId) {
        EcDeliveryAddress address = addressRepository.findById(id).orElseThrow();
        if (!address.getPatientId().equals(patientId)) {
            throw new SecurityException("Unauthorized access to address");
        }
        address.setIsDeleted(true);
        addressRepository.save(address);
    }
}
