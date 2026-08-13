package com.healthcare.clinic.engagement.repository;

import com.healthcare.clinic.engagement.entity.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long> {
    List<Reminder> findByPatientIdAndStatus(Long patientId, Reminder.ReminderStatus status);
    List<Reminder> findByStatus(Reminder.ReminderStatus status);
}
