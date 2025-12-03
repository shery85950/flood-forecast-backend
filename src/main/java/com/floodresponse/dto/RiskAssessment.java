package com.floodresponse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RiskAssessment {
    private String riskLevel; // Low Risk, Medium Risk, High Risk, Critical
    private String riverLevel; // Normal, Rising, High, Flood
    private String description;
    private List<String> recommendations;
    private double confidenceScore; // 0-100
}
