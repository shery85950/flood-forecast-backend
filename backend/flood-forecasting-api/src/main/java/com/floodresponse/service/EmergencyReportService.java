package com.floodresponse.service;

import com.floodresponse.model.EmergencyReport;
import com.floodresponse.repository.EmergencyReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmergencyReportService {

    @Autowired
    private EmergencyReportRepository reportRepository;

    public List<EmergencyReport> getAllReports() {
        return reportRepository.findByOrderByReportedAtDesc();
    }

    public Optional<EmergencyReport> getReportById(Long id) {
        return reportRepository.findById(id);
    }

    public EmergencyReport createReport(EmergencyReport report) {
        return reportRepository.save(report);
    }

    public EmergencyReport updateReportStatus(Long id, String status) {
        EmergencyReport report = reportRepository.findById(id).orElseThrow(() -> new RuntimeException("Report not found"));
        report.setStatus(status);
        return reportRepository.save(report);
    }

    public void deleteReport(Long id) {
        reportRepository.deleteById(id);
    }
}
