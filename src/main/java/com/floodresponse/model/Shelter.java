package com.floodresponse.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "shelters")
public class Shelter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;
    
    private double latitude;
    private double longitude;
    
    private int capacity;
    private int currentOccupancy;
    
    private String contactNumber;
    
    @Column(length = 500)
    private String facilities; // Comma separated list
    
    private boolean isOpen;
}
