package com.healthcare.clinic.superadmin.repository;
import com.healthcare.clinic.superadmin.entity.ActiveSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface ActiveSessionRepository extends JpaRepository<ActiveSession, Long> {
    List<ActiveSession> findByUserIdAndRevokedFalse(Long userId);
}
