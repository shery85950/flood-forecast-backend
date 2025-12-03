package com.floodresponse.service;

import com.floodresponse.dto.RiskAssessment;
import com.floodresponse.dto.WeatherData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationReportService {
    
    private static final Logger logger = LoggerFactory.getLogger(LocationReportService.class);
    
    @Autowired
    private WeatherDataService weatherDataService;
    
    @Autowired
    private GrokAiService grokAiService;
    
    /**
     * Generates AI-powered flood risk report for a specific location
     * This is called when users search for their location on the frontend
     */
    public RiskAssessment generateLocationReport(String location) {
        try {
            logger.info("Generating location report for: {}", location);
            
            // Fetch weather data for the specific location
            WeatherData weatherData = weatherDataService.getLocationForecast(location);
            
            // Use Grok AI to analyze the flood risk
            RiskAssessment assessment = grokAiService.analyzeFloodRisk(location, weatherData);
            
            logger.info("Successfully generated location report for {}: Risk Level = {}",
                    location, assessment.getRiskLevel());
            
            return assessment;
            
        } catch (Exception e) {
            logger.error("Error generating location report for: {}", location, e);
            throw new RuntimeException("Failed to generate location report for " + location, e);
        }
    }
    
    /**
     * Analyzes local flood risk with additional context
     */
    public RiskAssessment analyzeLocalFloodRisk(String location, WeatherData weatherData) {
        return grokAiService.analyzeFloodRisk(location, weatherData);
    }
}
