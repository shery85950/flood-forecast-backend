package com.floodresponse.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.floodresponse.dto.RiskAssessment;
import com.floodresponse.dto.WeatherData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class GrokAiService {
    
    private static final Logger logger = LoggerFactory.getLogger(GrokAiService.class);
    
    @Value("${grok.api.key}")
    private String apiKey;
    
    @Value("${grok.api.url}")
    private String apiUrl;
    
    @Value("${grok.model}")
    private String model;
    
    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    
    public GrokAiService(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder.build();
        this.objectMapper = objectMapper;
    }
    
    /**
     * Analyzes weather data and generates flood risk assessment using Grok AI
     */
    public RiskAssessment analyzeFloodRisk(String region, WeatherData weatherData) {
        try {
            logger.info("Analyzing flood risk for region: {}", region);
            
            String prompt = buildFloodRiskPrompt(region, weatherData);
            String aiResponse = callGrokApi(prompt);
            
            return parseRiskAssessment(aiResponse);
            
        } catch (Exception e) {
            logger.error("Error analyzing flood risk for region: {}", region, e);
            // Return a default assessment on error
            return createDefaultAssessment();
        }
    }
    
    /**
     * Builds a detailed prompt for Grok AI to analyze flood risk
     */
    private String buildFloodRiskPrompt(String region, WeatherData weatherData) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("You are a flood risk assessment expert for Pakistan. ");
        prompt.append("Analyze the following 7-day weather forecast data and provide a flood risk assessment.\n\n");
        
        prompt.append("Region: ").append(region).append("\n");
        prompt.append("Total Rainfall (7 days): ").append(String.format("%.1f", weatherData.getTotalRainfall())).append(" mm\n");
        prompt.append("Average Temperature: ").append(String.format("%.1f", weatherData.getAvgTemperature())).append("°C\n");
        prompt.append("Temperature Range: ").append(String.format("%.1f", weatherData.getMinTemperature()))
                .append("°C to ").append(String.format("%.1f", weatherData.getMaxTemperature())).append("°C\n\n");
        
        prompt.append("Daily Breakdown:\n");
        weatherData.getDailyForecasts().forEach(day -> {
            prompt.append(String.format("- %s: %.1fmm rain (%.0f%% chance), %s, Temp: %.1f°C\n",
                    day.getDate(), day.getTotalRainfall(), (double)day.getChanceOfRain(),
                    day.getCondition(), day.getAvgTemp()));
        });
        
        prompt.append("\nBased on this data, provide a JSON response with the following structure:\n");
        prompt.append("{\n");
        prompt.append("  \"riskLevel\": \"Low Risk\" | \"Medium Risk\" | \"High Risk\" | \"Critical\",\n");
        prompt.append("  \"riverLevel\": \"Normal\" | \"Rising\" | \"High\" | \"Flood\",\n");
        prompt.append("  \"description\": \"A brief 2-3 sentence description of the flood risk situation\",\n");
        prompt.append("  \"recommendations\": [\"recommendation 1\", \"recommendation 2\", \"recommendation 3\"],\n");
        prompt.append("  \"confidenceScore\": 0-100\n");
        prompt.append("}\n\n");
        prompt.append("Consider:\n");
        prompt.append("- Rainfall intensity and distribution\n");
        prompt.append("- Regional geography and flood history\n");
        prompt.append("- River systems in the area\n");
        prompt.append("- Monsoon season patterns\n");
        prompt.append("\nProvide ONLY the JSON response, no additional text.");
        
        return prompt.toString();
    }
    
    /**
     * Calls Grok AI API with the given prompt
     */
    private String callGrokApi(String prompt) {
        try {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", model);
            
            Map<String, String> message = new HashMap<>();
            message.put("role", "user");
            message.put("content", prompt);
            requestBody.put("messages", Arrays.asList(message));
            
            requestBody.put("temperature", 0.7);
            requestBody.put("max_tokens", 1000);
            
            String response = webClient.post()
                    .uri(apiUrl)
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            
            // Extract the content from the response
            JsonNode root = objectMapper.readTree(response);
            String content = root.path("choices").get(0).path("message").path("content").asText();
            
            logger.info("Received AI response: {}", content.substring(0, Math.min(100, content.length())));
            
            return content;
            
        } catch (Exception e) {
            logger.error("Error calling Grok AI API", e);
            throw new RuntimeException("Failed to call Grok AI API", e);
        }
    }
    
    /**
     * Parses AI response into RiskAssessment object
     */
    private RiskAssessment parseRiskAssessment(String aiResponse) {
        try {
            // Extract JSON from response (in case there's extra text)
            String jsonStr = aiResponse;
            int jsonStart = aiResponse.indexOf("{");
            int jsonEnd = aiResponse.lastIndexOf("}");
            if (jsonStart >= 0 && jsonEnd > jsonStart) {
                jsonStr = aiResponse.substring(jsonStart, jsonEnd + 1);
            }
            
            JsonNode root = objectMapper.readTree(jsonStr);
            
            RiskAssessment assessment = new RiskAssessment();
            assessment.setRiskLevel(root.path("riskLevel").asText("Medium Risk"));
            assessment.setRiverLevel(root.path("riverLevel").asText("Normal"));
            assessment.setDescription(root.path("description").asText(""));
            assessment.setConfidenceScore(root.path("confidenceScore").asDouble(75.0));
            
            // Parse recommendations array
            JsonNode recsNode = root.path("recommendations");
            ArrayList<String> recommendations = new ArrayList<>();
            if (recsNode.isArray()) {
                recsNode.forEach(rec -> recommendations.add(rec.asText()));
            }
            assessment.setRecommendations(recommendations);
            
            logger.info("Parsed risk assessment: {} - {}", assessment.getRiskLevel(), assessment.getRiverLevel());
            
            return assessment;
            
        } catch (Exception e) {
            logger.error("Error parsing AI response", e);
            return createDefaultAssessment();
        }
    }
    
    /**
     * Creates a default risk assessment when AI fails
     */
    private RiskAssessment createDefaultAssessment() {
        RiskAssessment assessment = new RiskAssessment();
        assessment.setRiskLevel("Medium Risk");
        assessment.setRiverLevel("Normal");
        assessment.setDescription("Unable to generate AI assessment. Please monitor weather conditions closely.");
        assessment.setRecommendations(Arrays.asList(
                "Monitor local weather updates",
                "Stay informed through official channels",
                "Prepare emergency supplies"
        ));
        assessment.setConfidenceScore(50.0);
        return assessment;
    }
}
