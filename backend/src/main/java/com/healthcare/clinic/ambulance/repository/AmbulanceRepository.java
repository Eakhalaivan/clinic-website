package com.healthcare.clinic.ambulance.repository;

import com.healthcare.clinic.ambulance.entity.Ambulance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.math.BigDecimal;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface AmbulanceRepository extends JpaRepository<Ambulance, Long> {
    List<Ambulance> findByIsActiveTrue();
    List<Ambulance> findByStatus(String status);
    
    @Query(value = "SELECT * FROM ambulances a WHERE a.status = 'AVAILABLE' AND a.is_active = true " +
           "ORDER BY (6371 * ACOS(COS(RADIANS(:lat)) * COS(RADIANS(a.current_latitude)) * COS(RADIANS(a.current_longitude) - RADIANS(:lng)) + SIN(RADIANS(:lat)) * SIN(RADIANS(a.current_latitude)))) ASC", nativeQuery = true)
    List<Ambulance> findNearestAvailable(@Param("lat") BigDecimal lat, @Param("lng") BigDecimal lng);
}
