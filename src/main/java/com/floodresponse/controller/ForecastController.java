package com.floodresponse.controller;

import com.floodresponse.model.Forecast;
import com.floodresponse.service.ForecastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/forecasts")
public class ForecastController {
    @Autowired
    private ForecastService forecastService;

    @Autowired
    private com.floodresponse.service.ForecastAutomationService automationService;

    @Autowired
    private com.floodresponse.service.LocationReportService locationReportService;

    // Generic endpoints first
    @GetMapping
    public List<Forecast> getAllForecasts() {
        return forecastService.getAllForecasts();
    }

    // More specific endpoints BEFORE generic path variables
    @GetMapping("/latest")
    public List<Forecast> getLatestForecasts() {
        return forecastService.getLatestForecasts();
    }

    @GetMapping("/location-report")
    public ResponseEntity<com.floodresponse.dto.RiskAssessment> getLocationReport(@RequestParam String location) {
        try {
            com.floodresponse.dto.RiskAssessment assessment = locationReportService.generateLocationReport(location);
            return ResponseEntity.ok(assessment);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/generate-automated")
    public ResponseEntity<String> generateAutomatedForecasts() {
        try {
            automationService.triggerManualGeneration();
            return ResponseEntity.ok("Automated forecast generation triggered successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    // Generic path variable AFTER specific endpoints
    @GetMapping("/{id}")
    public ResponseEntity<Forecast> getForecastById(@PathVariable Long id) {
        return forecastService.getForecastById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Forecast createForecast(@RequestBody Forecast forecast) {
        return forecastService.createForecast(forecast);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Forecast> updateForecast(@PathVariable Long id, @RequestBody Forecast forecast) {
        try {
            return ResponseEntity.ok(forecastService.updateForecast(id, forecast));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteForecast(@PathVariable Long id) {
        forecastService.deleteForecast(id);
        return ResponseEntity.ok().build();
    }
}
