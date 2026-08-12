package com.healthcare.clinic.support.repository;

import com.healthcare.clinic.support.entity.SpTicketAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpTicketAssignmentRepository extends JpaRepository<SpTicketAssignment, Long> {
}
