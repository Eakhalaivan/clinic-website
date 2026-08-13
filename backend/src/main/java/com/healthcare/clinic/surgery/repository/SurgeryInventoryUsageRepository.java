package com.healthcare.clinic.surgery.repository;

import com.healthcare.clinic.surgery.entity.SurgeryInventoryUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurgeryInventoryUsageRepository extends JpaRepository<SurgeryInventoryUsage, Long> {
    List<SurgeryInventoryUsage> findBySurgeryBookingId(Long surgeryBookingId);
}
