package com.healthcare.clinic.finance.repository;

import com.healthcare.clinic.finance.entity.ChartOfAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface ChartOfAccountRepository extends JpaRepository<ChartOfAccount, Long> {
    Optional<ChartOfAccount> findByAccountCode(String accountCode);
    List<ChartOfAccount> findByAccountType(ChartOfAccount.AccountType accountType);
}
