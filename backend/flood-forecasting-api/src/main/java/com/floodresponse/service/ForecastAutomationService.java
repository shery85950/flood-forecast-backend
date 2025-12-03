package com.floodresponse.service;

import com.floodresponse.dto.RiskAssessment;
import com.floodresponse.dto.WeatherData;
import com.floodresponse.model.Forecast;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class ForecastAutomationService {
    
    private static final Logger logger = LoggerFactory.getLogger(ForecastAutomationService.class);
    
    @Value("${forecast.automation.enabled}")
    private boolean automationEnabled;
    
    @Value("${forecast.regions}")
    private String regionsConfig;
    
    @Autowired
    private WeatherDataService weatherDataService;
    
    @Autowired
    private GrokAiService grokAiService;
    
    @Autowired
    private ForecastService forecastService;
    
    /**
     * Scheduled task to generate weekly forecasts
     * Runs every Monday at 6 AM (configured in application.properties)
     */
    @Scheduled(cron = "${forecast.automation.cron}")
    public void generateWeeklyForecasts() {
        if (!automationEnabled) {
            logger.info("Forecast automation is disabled");
            return;
        }
        
        logger.info("Starting automated weekly forecast generation...");
        
        try {
            List<String> regions = Arrays.asList(regionsConfig.split(","));
            
            for (String region : regions) {
                processRegion(region.trim());
            }
            
            logger.info("Completed automated weekly forecast generation for {} regions", regions.size());
            
        } catch (Exception e) {
            logger.error("Error during automated forecast generation", e);
        }
    }
    
    /**
     * Processes a single region: fetches weather data, analyzes with AI, and saves forecast
     */
    private void processRegion(String region) {
        try {
            logger.info("Processing region: {}", region);
            
            // Fetch weather data
            WeatherData weatherData = weatherDataService.getWeeklyForecast(region);
            
            // Analyze with Grok AI
            RiskAssessment assessment = grokAiService.analyzeFloodRisk(region, weatherData);
            
            // Save to database
            saveForecast(region, weatherData, assessment);
            
            logger.info("Successfully processed region: {} - Risk: {}", region, assessment.getRiskLevel());
            
        } catch (Exception e) {
            logger.error("Error processing region: {}", region, e);
        }
    }
    
    /**
     * Saves the AI-generated forecast to the database
     */
    private void saveForecast(String region, WeatherData weatherData, RiskAssessment assessment) {
        Forecast forecast = new Forecast();
        forecast.setRegion(region);
        forecast.setRiskLevel(assessment.getRiskLevel());
        forecast.setRiverLevel(assessment.getRiverLevel());
        forecast.setRainfall(weatherData.getTotalRainfall());
        forecast.setForecastDate(LocalDateTime.now().plusDays(7)); // Forecast for next week
        
        // Build description from AI assessment and recommendations
        StringBuilder description = new StringBuilder();
        description.append(assessment.getDescription());
        if (assessment.getRecommendations() != null && !assessment.getRecommendations().isEmpty()) {
            description.append("\n\nRecommendations:\n");
            for (String rec : assessment.getRecommendations()) {
                description.append("• ").append(rec).append("\n");
            }
        }
        description.append(String.format("\n[AI Confidence: %.0f%%]", assessment.getConfidenceScore()));
        
        forecast.setDescription(description.toString());
        
        forecastService.createForecast(forecast);
        
        logger.info("Saved forecast for region: {}", region);
    }
    
    /**
     * Manual trigger for testing purposes
     */
    public void triggerManualGeneration() {
        logger.info("Manually triggered forecast generation");
        generateWeeklyForecasts();
    }
}
