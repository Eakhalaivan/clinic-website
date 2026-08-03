package com.healthcare.clinic.branch.controller;

import com.healthcare.clinic.branch.entity.Branch;
import com.healthcare.clinic.branch.service.BranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/branches")
@RequiredArgsConstructor
public class BranchController {

    private final BranchService branchService;

    @GetMapping
    public ResponseEntity<List<Branch>> getActiveBranches() {
        return ResponseEntity.ok(branchService.getAllActiveBranches());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Branch> getBranchById(@PathVariable Long id) {
        return branchService.getBranchById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<Branch> createBranch(@jakarta.validation.Valid @RequestBody Branch branch) {
        Branch created = branchService.createOrUpdateBranch(branch);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SUPER_ADMIN', 'ROLE_BRANCH_ADMIN')")
    public ResponseEntity<Branch> updateBranch(@PathVariable Long id, @jakarta.validation.Valid @RequestBody Branch branch) {
        // Only admins can change any branch. Branch admins can only change their own branch.
        com.healthcare.clinic.security.SecurityUtils.assertBranchAdmin(id);
        
        branch.setId(id);
        Branch updated = branchService.createOrUpdateBranch(branch);
        return ResponseEntity.ok(updated);
    }
}
