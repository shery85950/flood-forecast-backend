package com.floodresponse.repository;

import com.floodresponse.model.EmergencyReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmergencyReportRepository extends JpaRepository<EmergencyReport, Long> {
    List<EmergencyReport> findByStatus(String status);
    List<EmergencyReport> findByOrderByReportedAtDesc();
}
