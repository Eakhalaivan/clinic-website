package com.healthcare.clinic.finance.repository;

import com.healthcare.clinic.finance.entity.BranchBudget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface BranchBudgetRepository extends JpaRepository<BranchBudget, Long> {
    
    Optional<BranchBudget> findByBranchIdAndBudgetYearAndBudgetMonth(Long branchId, Integer budgetYear, Integer budgetMonth);
    
    List<BranchBudget> findByBranchIdOrderByBudgetYearDescBudgetMonthDesc(Long branchId);
}
