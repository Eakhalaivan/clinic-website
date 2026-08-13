package com.healthcare.clinic.inpatient.repository;

import com.healthcare.clinic.inpatient.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
}
