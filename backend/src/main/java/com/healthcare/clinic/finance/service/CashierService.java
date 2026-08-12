package com.healthcare.clinic.finance.service;

import com.healthcare.clinic.branch.entity.Branch;
import com.healthcare.clinic.branch.repository.BranchRepository;
import com.healthcare.clinic.finance.entity.CashierSession;
import com.healthcare.clinic.finance.repository.CashierSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class CashierService {

    private final CashierSessionRepository cashierSessionRepository;
    private final BranchRepository branchRepository;

    @Transactional
    public CashierSession openSession(Long branchId, Long cashierId, BigDecimal openingFloat) {
        // Check if there's already an open session for this cashier
        cashierSessionRepository.findByCashierIdAndStatus(cashierId, CashierSession.SessionStatus.OPEN)
                .ifPresent(s -> {
                    throw new IllegalStateException("Cashier already has an open session");
                });

        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new IllegalArgumentException("Branch not found"));

        CashierSession session = CashierSession.builder()
                .branch(branch)
                .cashierId(cashierId)
                .openingFloat(openingFloat)
                .status(CashierSession.SessionStatus.OPEN)
                .build();

        return cashierSessionRepository.save(session);
    }

    @Transactional
    public CashierSession closeSession(Long sessionId, BigDecimal closingFloat) {
        CashierSession session = cashierSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found"));

        if (session.getStatus() != CashierSession.SessionStatus.OPEN) {
            throw new IllegalStateException("Session is not open");
        }

        session.setClosingFloat(closingFloat);
        session.setClosedAt(ZonedDateTime.now());

        // Calculate expected float
        BigDecimal expectedFloat = session.getOpeningFloat()
                .add(session.getCashCollections())
                .subtract(session.getRefundsIssued());

        BigDecimal variance = closingFloat.subtract(expectedFloat);
        session.setVarianceAmount(variance);

        if (variance.compareTo(BigDecimal.ZERO) == 0) {
            session.setStatus(CashierSession.SessionStatus.CLOSED);
        } else {
            session.setStatus(CashierSession.SessionStatus.DISCREPANCY);
            log.warn("Discrepancy in cashier session {}. Expected: {}, Actual: {}", sessionId, expectedFloat, closingFloat);
        }

        return cashierSessionRepository.save(session);
    }

    @Transactional
    public void recordCashCollection(Long cashierId, BigDecimal amount) {
        CashierSession session = cashierSessionRepository.findByCashierIdAndStatus(cashierId, CashierSession.SessionStatus.OPEN)
                .orElseThrow(() -> new IllegalStateException("No open session for cashier"));
        
        session.setCashCollections(session.getCashCollections().add(amount));
        cashierSessionRepository.save(session);
    }
}
