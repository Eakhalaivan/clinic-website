package com.healthcare.clinic.ambulance.repository;

import com.healthcare.clinic.ambulance.entity.Ambulance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AmbulanceRepository extends JpaRepository<Ambulance, Long> {
    List<Ambulance> findByIsActiveTrue();
    List<Ambulance> findByStatus(String status);
}
