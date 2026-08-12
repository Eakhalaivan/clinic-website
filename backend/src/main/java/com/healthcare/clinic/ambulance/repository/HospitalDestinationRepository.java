package com.healthcare.clinic.ambulance.repository;

import com.healthcare.clinic.ambulance.entity.HospitalDestination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalDestinationRepository extends JpaRepository<HospitalDestination, Long> {
}
