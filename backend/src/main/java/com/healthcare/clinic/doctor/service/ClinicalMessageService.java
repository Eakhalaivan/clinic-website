package com.healthcare.clinic.doctor.service;

import com.healthcare.clinic.doctor.entity.ClinicalMessage;
import com.healthcare.clinic.doctor.repository.ClinicalMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClinicalMessageService {

    private final ClinicalMessageRepository messageRepository;

    @Transactional
    public ClinicalMessage sendMessage(ClinicalMessage message) {
        message.setIsRead(false);
        return messageRepository.save(message);
    }

    public List<ClinicalMessage> getInbox(Long doctorId) {
        return messageRepository.findByRecipientIdOrderByCreatedAtDesc(doctorId);
    }

    public List<ClinicalMessage> getSentMessages(Long doctorId) {
        return messageRepository.findBySenderIdOrderByCreatedAtDesc(doctorId);
    }

    public List<ClinicalMessage> getMessagesForPatient(Long patientId) {
        return messageRepository.findByPatientIdOrderByCreatedAtDesc(patientId);
    }

    @Transactional
    public void markAsRead(Long messageId) {
        messageRepository.findById(messageId).ifPresent(msg -> {
            msg.setIsRead(true);
            msg.setReadAt(ZonedDateTime.now());
            messageRepository.save(msg);
        });
    }
}
