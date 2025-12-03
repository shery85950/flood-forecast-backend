package com.floodresponse.service;

import com.floodresponse.model.Alert;
import com.floodresponse.repository.AlertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlertService {

    @Autowired
    private AlertRepository alertRepository;

    public List<Alert> getAllAlerts() {
        return alertRepository.findAll();
    }

    public List<Alert> getActiveAlerts() {
        return alertRepository.findByIsActiveTrue();
    }

    public Optional<Alert> getAlertById(Long id) {
        return alertRepository.findById(id);
    }

    public Alert createAlert(Alert alert) {
        return alertRepository.save(alert);
    }

    public Alert updateAlert(Long id, Alert alertDetails) {
        Alert alert = alertRepository.findById(id).orElseThrow(() -> new RuntimeException("Alert not found"));
        alert.setTitle(alertDetails.getTitle());
        alert.setDescription(alertDetails.getDescription());
        alert.setSeverity(alertDetails.getSeverity());
        alert.setRegion(alertDetails.getRegion());
        alert.setExpiresAt(alertDetails.getExpiresAt());
        alert.setActive(alertDetails.isActive());
        return alertRepository.save(alert);
    }

    public void deleteAlert(Long id) {
        alertRepository.deleteById(id);
    }
}
