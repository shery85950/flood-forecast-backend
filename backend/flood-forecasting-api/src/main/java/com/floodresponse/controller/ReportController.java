package com.floodresponse.controller;

import com.floodresponse.model.EmergencyReport;
import com.floodresponse.service.EmergencyReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private EmergencyReportService reportService;

    @GetMapping
    public List<EmergencyReport> getAllReports() {
        return reportService.getAllReports();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmergencyReport> getReportById(@PathVariable Long id) {
        return reportService.getReportById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public EmergencyReport createReport(@RequestBody EmergencyReport report) {
        return reportService.createReport(report);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<EmergencyReport> updateReportStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            return ResponseEntity.ok(reportService.updateReportStatus(id, status));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {
        reportService.deleteReport(id);
        return ResponseEntity.ok().build();
    }
}
