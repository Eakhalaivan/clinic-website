package com.healthcare.clinic.finance.service;

import com.healthcare.clinic.finance.entity.ChartOfAccount;
import com.healthcare.clinic.finance.entity.JournalEntry;
import com.healthcare.clinic.finance.entity.LedgerEntry;
import com.healthcare.clinic.finance.repository.ChartOfAccountRepository;
import com.healthcare.clinic.finance.repository.JournalEntryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeneralLedgerService {

    private final JournalEntryRepository journalEntryRepository;
    private final ChartOfAccountRepository accountRepository;

    @Transactional
    public JournalEntry postDoubleEntry(String description, LocalDate entryDate, String referenceType, Long referenceId,
                                        Long debitAccountId, BigDecimal debitAmount,
                                        Long creditAccountId, BigDecimal creditAmount,
                                        Long branchId, Long preparedBy) {

        if (debitAmount.compareTo(creditAmount) != 0) {
            throw new IllegalArgumentException("Journal Entry is unbalanced: Debits do not equal Credits.");
        }

        ChartOfAccount debitAccount = accountRepository.findById(debitAccountId)
                .orElseThrow(() -> new IllegalArgumentException("Debit account not found"));
        ChartOfAccount creditAccount = accountRepository.findById(creditAccountId)
                .orElseThrow(() -> new IllegalArgumentException("Credit account not found"));

        JournalEntry journalEntry = JournalEntry.builder()
                .journalNumber("JNL-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .entryDate(entryDate)
                .description(description)
                .referenceType(referenceType)
                .referenceId(referenceId)
                .preparedBy(preparedBy)
                .status(JournalEntry.JournalStatus.POSTED)
                .build();

        LedgerEntry debitLine = LedgerEntry.builder()
                .journalEntry(journalEntry)
                .account(debitAccount)
                .debitAmount(debitAmount)
                .creditAmount(BigDecimal.ZERO)
                .description("Debit: " + description)
                .branchId(branchId)
                .build();

        LedgerEntry creditLine = LedgerEntry.builder()
                .journalEntry(journalEntry)
                .account(creditAccount)
                .debitAmount(BigDecimal.ZERO)
                .creditAmount(creditAmount)
                .description("Credit: " + description)
                .branchId(branchId)
                .build();

        journalEntry.getLines().add(debitLine);
        journalEntry.getLines().add(creditLine);

        return journalEntryRepository.save(journalEntry);
    }
    
    @Transactional
    public JournalEntry postPaymentReceived(BigDecimal amount, Long invoiceId, Long branchId, Long cashierId) {
        // Find default Cash/Bank account and Accounts Receivable
        // This is a simplified automated posting
        ChartOfAccount cashAccount = accountRepository.findByAccountCode("1001").orElse(null);
        ChartOfAccount arAccount = accountRepository.findByAccountCode("1200").orElse(null);
        
        if (cashAccount == null || arAccount == null) {
            log.warn("Default accounts for payment received not configured");
            return null;
        }
        
        return postDoubleEntry("Payment for Invoice " + invoiceId, LocalDate.now(), "PAYMENT", invoiceId,
                cashAccount.getId(), amount, arAccount.getId(), amount, branchId, cashierId);
    }

    @Transactional
    public JournalEntry postExpensePaid(BigDecimal amount, Long expenseId, Long branchId, Long payerId, String expenseCategory) {
        ChartOfAccount cashAccount = accountRepository.findByAccountCode("1001").orElse(null);
        ChartOfAccount expenseAccount = accountRepository.findByAccountCode("5000").orElse(null); // General Expense
        
        if (cashAccount == null || expenseAccount == null) {
            log.warn("Default accounts for expenses not configured");
            return null;
        }
        
        return postDoubleEntry("Payment for Expense " + expenseId + " (" + expenseCategory + ")", LocalDate.now(), "EXPENSE", expenseId,
                expenseAccount.getId(), amount, cashAccount.getId(), amount, branchId, payerId);
    }

    @Transactional(readOnly = true)
    public List<JournalEntry> getAllJournals() {
        return journalEntryRepository.findAll();
    }
}
