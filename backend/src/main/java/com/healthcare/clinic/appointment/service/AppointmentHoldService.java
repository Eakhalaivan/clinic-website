package com.healthcare.clinic.appointment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentHoldService {

    private final StringRedisTemplate redisTemplate;
    private static final Duration HOLD_DURATION = Duration.ofMinutes(3);

    public String holdSlot(Long doctorId, String slotStart) {
        String key = "slot-hold:" + doctorId + ":" + slotStart;
        String holdId = UUID.randomUUID().toString();

        Boolean acquired = redisTemplate.opsForValue().setIfAbsent(key, holdId, HOLD_DURATION);
        
        if (Boolean.TRUE.equals(acquired)) {
            log.info("Acquired hold {} for doctor {} at {}", holdId, doctorId, slotStart);
            return holdId;
        } else {
            log.warn("Failed to acquire hold for doctor {} at {}", doctorId, slotStart);
            return null; // Indicates slot is already held
        }
    }

    public boolean validateHold(Long doctorId, String slotStart, String holdId) {
        if (holdId == null) return false;
        String key = "slot-hold:" + doctorId + ":" + slotStart;
        String existingHoldId = redisTemplate.opsForValue().get(key);
        return holdId.equals(existingHoldId);
    }

    public void releaseHold(Long doctorId, String slotStart, String holdId) {
        if (holdId == null) return;
        String key = "slot-hold:" + doctorId + ":" + slotStart;
        String existingHoldId = redisTemplate.opsForValue().get(key);
        
        if (holdId.equals(existingHoldId)) {
            redisTemplate.delete(key);
            log.info("Released hold {} for doctor {} at {}", holdId, doctorId, slotStart);
        }
    }

    public boolean isHeld(Long doctorId, String slotStart) {
        String key = "slot-hold:" + doctorId + ":" + slotStart;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    public boolean isIdempotencyKeyProcessed(String idempotencyKey) {
        if (idempotencyKey == null || idempotencyKey.isEmpty()) return false;
        return Boolean.TRUE.equals(redisTemplate.hasKey("idemp:" + idempotencyKey));
    }

    public Long getAppointmentIdForIdempotencyKey(String idempotencyKey) {
        if (idempotencyKey == null || idempotencyKey.isEmpty()) return null;
        String val = redisTemplate.opsForValue().get("idemp:" + idempotencyKey);
        if (val != null) {
            return Long.parseLong(val);
        }
        return null;
    }

    public void saveIdempotencyKey(String idempotencyKey, Long appointmentId) {
        if (idempotencyKey == null || idempotencyKey.isEmpty()) return;
        redisTemplate.opsForValue().set("idemp:" + idempotencyKey, String.valueOf(appointmentId), Duration.ofHours(24));
    }
}
