package com.healthcare.clinic.telemedicine.repository;
import com.healthcare.clinic.telemedicine.entity.TeleconsultSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
@Repository
public interface TeleconsultSessionRepository extends JpaRepository<TeleconsultSession, Long> {
    Optional<TeleconsultSession> findByAppointmentId(Long appointmentId);
    Optional<TeleconsultSession> findByRoomId(String roomId);
}
