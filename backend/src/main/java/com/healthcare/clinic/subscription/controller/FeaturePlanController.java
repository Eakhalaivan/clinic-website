package com.healthcare.clinic.subscription.controller;

import com.healthcare.clinic.subscription.entity.FeaturePlan;
import com.healthcare.clinic.subscription.service.FeaturePlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptions/featureplans")
@RequiredArgsConstructor
public class FeaturePlanController {

    private final FeaturePlanService service;

    @GetMapping
    public ResponseEntity<List<FeaturePlan>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FeaturePlan> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<FeaturePlan> create(@RequestBody FeaturePlan entity) {
        return ResponseEntity.ok(service.save(entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
