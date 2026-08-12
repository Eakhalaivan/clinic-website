package com.healthcare.clinic.doctor.medicine.repository;

import com.healthcare.clinic.doctor.medicine.entity.MedicineOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicineOrderItemRepository extends JpaRepository<MedicineOrderItem, Long> {
}
