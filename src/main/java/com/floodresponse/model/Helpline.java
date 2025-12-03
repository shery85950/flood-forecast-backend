package com.floodresponse.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "helplines")
public class Helpline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // e.g., Rescue 1122
    private String phoneNumber;
    private String category; // Rescue, Medical, Police, NGO
    private String region; // Nationwide, Punjab, etc.
    private String description;
}
