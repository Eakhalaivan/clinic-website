package com.healthcare.clinic.inventory.pharmacy.service;

import com.healthcare.clinic.inventory.pharmacy.entity.PharmacyClearance;
import com.healthcare.clinic.inventory.pharmacy.repository.PharmacyClearanceRepository;
import com.healthcare.clinic.identity.entity.User;
import com.healthcare.clinic.identity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PharmacyClearanceService {

    private final PharmacyClearanceRepository pharmacyClearanceRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<PharmacyClearance> getAllClearances() {
        return pharmacyClearanceRepository.findAll();
    }

    @Transactional
    public PharmacyClearance markAsCleared(Long id, String userEmail) {
        PharmacyClearance clearance = pharmacyClearanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Clearance record not found"));

        if ("Cleared".equals(clearance.getStatus())) {
            return clearance;
        }

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        clearance.setStatus("Cleared");
        clearance.setClearedAt(ZonedDateTime.now());
        clearance.setClearedBy(user);
        
        return pharmacyClearanceRepository.save(clearance);
    }
}
