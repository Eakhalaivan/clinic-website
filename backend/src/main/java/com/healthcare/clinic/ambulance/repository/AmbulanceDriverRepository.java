package com.healthcare.clinic.ambulance.repository;

import com.healthcare.clinic.ambulance.entity.AmbulanceDriver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AmbulanceDriverRepository extends JpaRepository<AmbulanceDriver, Long> {
}
