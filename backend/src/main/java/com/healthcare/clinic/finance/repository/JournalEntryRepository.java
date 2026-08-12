package com.healthcare.clinic.finance.repository;

import com.healthcare.clinic.finance.entity.JournalEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long> {
    Optional<JournalEntry> findByJournalNumber(String journalNumber);
}
