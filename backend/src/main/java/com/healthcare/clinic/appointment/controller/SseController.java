package com.healthcare.clinic.appointment.controller;

import com.healthcare.clinic.appointment.event.AppointmentBookedEvent;
import com.healthcare.clinic.appointment.event.AppointmentStatusChangedEvent;
import com.healthcare.clinic.notification.event.AppointmentCancelledEvent;
import com.healthcare.clinic.notification.event.QueueTokenCalledEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/api/sse/appointments")
@Slf4j
public class SseController {

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter(60 * 60 * 1000L); // 1 hour timeout
        emitters.add(emitter);

        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError(e -> emitters.remove(emitter));

        return emitter;
    }

    @EventListener
    public void onAppointmentBooked(AppointmentBookedEvent event) {
        broadcastEvent("appointment-booked", event);
    }

    @EventListener
    public void onAppointmentCancelled(AppointmentCancelledEvent event) {
        broadcastEvent("appointment-cancelled", event);
    }

    @EventListener
    public void onAppointmentStatusChanged(AppointmentStatusChangedEvent event) {
        broadcastEvent("appointment-status-changed", event);
    }

    @EventListener
    public void onQueueTokenCalled(QueueTokenCalledEvent event) {
        broadcastEvent("queue-token-called", event);
    }

    private void broadcastEvent(String name, Object eventData) {
        List<SseEmitter> deadEmitters = new CopyOnWriteArrayList<>();
        emitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event()
                        .name(name)
                        .data(eventData));
            } catch (IOException e) {
                deadEmitters.add(emitter);
            }
        });
        emitters.removeAll(deadEmitters);
    }
}
