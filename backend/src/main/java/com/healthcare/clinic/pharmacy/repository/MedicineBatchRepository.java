package com.healthcare.clinic.pharmacy.repository;


import com.healthcare.clinic.pharmacy.entity.MedicineBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicineBatchRepository extends JpaRepository<MedicineBatch, Long> {
    org.springframework.data.domain.Page<MedicineBatch> findByMedicineId(Long medicineId, org.springframework.data.domain.Pageable pageable);
}
