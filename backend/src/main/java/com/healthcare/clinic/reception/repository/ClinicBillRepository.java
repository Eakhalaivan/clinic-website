package com.healthcare.clinic.reception.repository;

import com.healthcare.clinic.reception.entity.ClinicBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClinicBillRepository extends JpaRepository<ClinicBill, Long> {
    List<ClinicBill> findByPatientId(Long patientId);
    List<ClinicBill> findByAppointmentId(Long appointmentId);
}
