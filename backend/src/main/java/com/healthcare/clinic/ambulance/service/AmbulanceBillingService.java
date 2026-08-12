package com.healthcare.clinic.ambulance.service;

import com.healthcare.clinic.ambulance.entity.AmbulanceTripBilling;
import com.healthcare.clinic.ambulance.repository.AmbulanceTripBillingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AmbulanceBillingService {
    private final AmbulanceTripBillingRepository billingRepository;

    @Transactional
    public AmbulanceTripBilling createBill(AmbulanceTripBilling bill) {
        return billingRepository.save(bill);
    }
}
