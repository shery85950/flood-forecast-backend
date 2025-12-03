package com.floodresponse.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "emergency_reports")
public class EmergencyReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reporterName;
    private String contactNumber;
    
    private String location;
    private Double latitude;
    private Double longitude;
    
    private String emergencyType; // Flood, Medical, Rescue, etc.
    
    @Column(length = 1000)
    private String description;
    
    private String status; // Pending, In Progress, Resolved
    
    private LocalDateTime reportedAt;
    
    @PrePersist
    protected void onCreate() {
        reportedAt = LocalDateTime.now();
        status = "Pending";
    }
}
