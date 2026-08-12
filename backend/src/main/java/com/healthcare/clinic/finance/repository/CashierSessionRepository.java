package com.healthcare.clinic.finance.repository;

import com.healthcare.clinic.finance.entity.CashierSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface CashierSessionRepository extends JpaRepository<CashierSession, Long> {
    Optional<CashierSession> findByCashierIdAndStatus(Long cashierId, CashierSession.SessionStatus status);
    List<CashierSession> findByBranchIdAndStatus(Long branchId, CashierSession.SessionStatus status);
}
