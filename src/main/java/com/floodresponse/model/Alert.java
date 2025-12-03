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
@Table(name = "alerts")
public class Alert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    
    @Column(length = 1000)
    private String description;
    
    private String severity; // Critical, Warning, Advisory
    private String region;
    
    private LocalDateTime issuedAt;
    private LocalDateTime expiresAt;
    
    private boolean isActive;
    
    @PrePersist
    protected void onCreate() {
        issuedAt = LocalDateTime.now();
        isActive = true;
    }
}
