package com.healthcare.clinic.doctor.medicine.repository;

import com.healthcare.clinic.doctor.medicine.entity.MedicineOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicineOrderRepository extends JpaRepository<MedicineOrder, Long> {
    List<MedicineOrder> findAllByPatientIdOrderByCreatedAtDesc(Long patientId);
    List<MedicineOrder> findAllByDoctorIdOrderByCreatedAtDesc(Long doctorId);
}
