package com.healthcare.clinic.ambulance.repository;

import com.healthcare.clinic.ambulance.entity.AmbulanceParamedic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AmbulanceParamedicRepository extends JpaRepository<AmbulanceParamedic, Long> {
}
