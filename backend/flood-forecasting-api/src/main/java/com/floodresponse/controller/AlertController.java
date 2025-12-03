package com.floodresponse.controller;

import com.floodresponse.model.Alert;
import com.floodresponse.service.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {

    @Autowired
    private AlertService alertService;

    @GetMapping
    public List<Alert> getAllAlerts() {
        return alertService.getAllAlerts();
    }

    @GetMapping("/active")
    public List<Alert> getActiveAlerts() {
        return alertService.getActiveAlerts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Alert> getAlertById(@PathVariable Long id) {
        return alertService.getAlertById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Alert createAlert(@RequestBody Alert alert) {
        return alertService.createAlert(alert);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Alert> updateAlert(@PathVariable Long id, @RequestBody Alert alert) {
        try {
            return ResponseEntity.ok(alertService.updateAlert(id, alert));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlert(@PathVariable Long id) {
        alertService.deleteAlert(id);
        return ResponseEntity.ok().build();
    }
}
