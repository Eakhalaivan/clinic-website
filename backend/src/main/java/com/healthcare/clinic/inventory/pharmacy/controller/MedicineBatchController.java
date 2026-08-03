package com.healthcare.clinic.inventory.pharmacy.controller;

import com.healthcare.clinic.inventory.pharmacy.entity.MedicineBatch;
import com.healthcare.clinic.inventory.pharmacy.repository.MedicineBatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pharmacy/batches")
@RequiredArgsConstructor
@PreAuthorize("hasRole('PHARMACIST') or hasRole('SUPER_ADMIN')")
public class MedicineBatchController {

    private final MedicineBatchRepository medicineBatchRepository;

    @GetMapping("/medicine/{medicineId}")
    public ResponseEntity<org.springframework.data.domain.Page<MedicineBatch>> getBatchesForMedicine(
            @PathVariable Long medicineId, 
            @org.springframework.data.web.PageableDefault(size = 20) org.springframework.data.domain.Pageable pageable) {
        return ResponseEntity.ok(medicineBatchRepository.findByMedicineId(medicineId, pageable));
    }

    @PostMapping
    public ResponseEntity<MedicineBatch> createBatch(@RequestBody MedicineBatch batch) {
        return ResponseEntity.ok(medicineBatchRepository.save(batch));
    }
}
