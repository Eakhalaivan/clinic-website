package com.healthcare.clinic.support.repository;

import com.healthcare.clinic.support.entity.SpTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpTicketRepository extends JpaRepository<SpTicket, Long> {
    Optional<SpTicket> findByIdempotencyKey(String idempotencyKey);
    List<SpTicket> findByRequesterIdOrderByCreatedAtDesc(Long requesterId);
    List<SpTicket> findByStatusIn(List<String> statuses);
}
