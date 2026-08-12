package com.healthcare.clinic.ambulance.repository;

import com.healthcare.clinic.ambulance.entity.TripHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripHistoryRepository extends JpaRepository<TripHistory, Long> {
}
