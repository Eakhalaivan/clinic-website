package com.healthcare.clinic.ambulance.service;

import com.healthcare.clinic.ambulance.entity.Ambulance;
import com.healthcare.clinic.ambulance.repository.AmbulanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AmbulanceTrackingService {
    private final AmbulanceRepository ambulanceRepository;
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter(0L); // Infinite timeout
        this.emitters.add(emitter);
        
        emitter.onCompletion(() -> this.emitters.remove(emitter));
        emitter.onTimeout(() -> this.emitters.remove(emitter));
        
        return emitter;
    }

    private void broadcastLocation(Ambulance amb) {
        List<SseEmitter> deadEmitters = new java.util.ArrayList<>();
        this.emitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event()
                        .name("location-update")
                        .data(amb));
            } catch (IOException e) {
                deadEmitters.add(emitter);
            }
        });
        this.emitters.removeAll(deadEmitters);
    }

    @Transactional
    public void updateLocation(Long ambulanceId, BigDecimal lat, BigDecimal lng) {
        Ambulance amb = ambulanceRepository.findById(ambulanceId).orElseThrow();
        amb.setCurrentLatitude(lat);
        amb.setCurrentLongitude(lng);
        Ambulance saved = ambulanceRepository.save(amb);
        broadcastLocation(saved);
    }
}
