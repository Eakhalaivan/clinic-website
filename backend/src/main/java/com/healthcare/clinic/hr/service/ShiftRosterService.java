package com.healthcare.clinic.hr.service;

import com.healthcare.clinic.hr.entity.EmployeeRoster;
import com.healthcare.clinic.hr.repository.EmployeeRosterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class ShiftRosterService {

    private final EmployeeRosterRepository rosterRepository;

    public ShiftRosterService(EmployeeRosterRepository rosterRepository) {
        this.rosterRepository = rosterRepository;
    }

    public EmployeeRoster createRoster(EmployeeRoster roster) {
        // Prevent overlapping shifts for the same day
        List<EmployeeRoster> existing = rosterRepository.findByEmployeeIdAndRosterDate(
                roster.getEmployee().getId(), roster.getRosterDate());
        
        if (!existing.isEmpty()) {
            throw new IllegalStateException("Employee is already rostered on this date");
        }

        return rosterRepository.save(roster);
    }

    public List<EmployeeRoster> getRostersForBranch(Long branchId, LocalDate startDate, LocalDate endDate) {
        return rosterRepository.findByBranchIdAndRosterDateBetween(branchId, startDate, endDate);
    }

    public EmployeeRoster updateRosterStatus(Long rosterId, String status) {
        EmployeeRoster roster = rosterRepository.findById(rosterId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid roster ID"));
        roster.setStatus(status);
        return rosterRepository.save(roster);
    }
}
