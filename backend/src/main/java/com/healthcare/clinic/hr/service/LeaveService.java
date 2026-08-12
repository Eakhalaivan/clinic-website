package com.healthcare.clinic.hr.service;

import com.healthcare.clinic.hr.entity.LeaveBalance;
import com.healthcare.clinic.hr.entity.LeavePolicy;
import com.healthcare.clinic.hr.repository.LeaveBalanceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class LeaveService {

    private final LeaveBalanceRepository balanceRepository;

    public LeaveService(LeaveBalanceRepository balanceRepository) {
        this.balanceRepository = balanceRepository;
    }

    public LeaveBalance allocateLeave(Long employeeId, LeavePolicy policy, Integer year) {
        LeaveBalance balance = balanceRepository.findByEmployeeIdAndLeavePolicyIdAndYear(employeeId, policy.getId(), year)
                .orElseGet(() -> {
                    LeaveBalance newBalance = new LeaveBalance();
                    // Assuming Employee is set later or fetched
                    newBalance.setLeavePolicy(policy);
                    newBalance.setYear(year);
                    return newBalance;
                });
        
        balance.setAccrued(balance.getAccrued().add(policy.getAnnualAllocation()));
        balance.setBalance(balance.getBalance().add(policy.getAnnualAllocation()));
        
        return balanceRepository.save(balance);
    }

    public List<LeaveBalance> getBalances(Long employeeId, Integer year) {
        return balanceRepository.findByEmployeeIdAndYear(employeeId, year);
    }

    public void deductLeave(Long employeeId, Long policyId, Integer year, BigDecimal days) {
        LeaveBalance balance = balanceRepository.findByEmployeeIdAndLeavePolicyIdAndYear(employeeId, policyId, year)
                .orElseThrow(() -> new IllegalArgumentException("Leave balance not found"));

        if (balance.getBalance().compareTo(days) < 0) {
            throw new IllegalStateException("Insufficient leave balance");
        }

        balance.setBalance(balance.getBalance().subtract(days));
        balance.setTaken(balance.getTaken().add(days));
        balanceRepository.save(balance);
    }
}
