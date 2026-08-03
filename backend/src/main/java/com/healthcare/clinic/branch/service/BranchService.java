package com.healthcare.clinic.branch.service;

import com.healthcare.clinic.branch.entity.Branch;
import com.healthcare.clinic.branch.repository.BranchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BranchService {

    private final BranchRepository branchRepository;

    @Transactional(readOnly = true)
    public List<Branch> getAllActiveBranches() {
        List<Branch> branches = branchRepository.findAll();
        branches.forEach(b -> {
            if (b.getIsActive() == null) b.setIsActive(true);
            b.setOperatingHours(null);
            b.setStaffAssignments(null);
        });
        return branches;
    }

    @Transactional(readOnly = true)
    public Optional<Branch> getBranchById(Long id) {
        return branchRepository.findById(id);
    }

    @Transactional
    public Branch createOrUpdateBranch(Branch branch) {
        if (branch.getState() == null || branch.getState().isBlank()) {
            branch.setState("N/A");
        }
        if (branch.getAddress() == null || branch.getAddress().isBlank()) {
            branch.setAddress("N/A");
        }
        if (branch.getPostalCode() == null || branch.getPostalCode().isBlank()) {
            branch.setPostalCode("00000");
        }
        if (branch.getTimezone() == null || branch.getTimezone().isBlank()) {
            branch.setTimezone("UTC");
        }
        if (branch.getCountry() == null || branch.getCountry().isBlank()) {
            branch.setCountry("USA");
        }
        if (branch.getCity() == null || branch.getCity().isBlank()) {
            branch.setCity("Main");
        }

        // If operating hours are provided, ensure bidirectional link
        if (branch.getOperatingHours() != null) {
            branch.getOperatingHours().forEach(oh -> oh.setBranch(branch));
        }
        if (branch.getStaffAssignments() != null) {
            branch.getStaffAssignments().forEach(sa -> sa.setBranch(branch));
        }
        return branchRepository.save(branch);
    }
}
