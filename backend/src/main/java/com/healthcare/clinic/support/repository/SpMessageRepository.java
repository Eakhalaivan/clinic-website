package com.healthcare.clinic.support.repository;

import com.healthcare.clinic.support.entity.SpMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpMessageRepository extends JpaRepository<SpMessage, Long> {
    List<SpMessage> findByTicketIdOrderByCreatedAtAsc(Long ticketId);
}
