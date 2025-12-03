package com.floodresponse.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.floodresponse.dto.DailyForecast;
import com.floodresponse.dto.WeatherData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherDataService {
    
    private static final Logger logger = LoggerFactory.getLogger(WeatherDataService.class);
    
    @Value("${weather.api.key}")
    private String apiKey;
    
    @Value("${weather.api.url}")
    private String apiUrl;
    
    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    
    public WeatherDataService(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder.build();
        this.objectMapper = objectMapper;
    }
    
    /**
     * Fetches 7-day weather forecast for a specific region
     */
    public WeatherData getWeeklyForecast(String region) {
        try {
            logger.info("Fetching weekly forecast for region: {}", region);
            
            String url = String.format("%s/forecast.json?key=%s&q=%s&days=7&aqi=no&alerts=no",
                    apiUrl, apiKey, region);
            
            String response = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            
            return extractWeatherData(response, region);
            
        } catch (Exception e) {
            logger.error("Error fetching weather data for region: {}", region, e);
            throw new RuntimeException("Failed to fetch weather data for " + region, e);
        }
    }
    
    /**
     * Fetches weather forecast for a specific location (for user searches)
     */
    public WeatherData getLocationForecast(String location) {
        try {
            logger.info("Fetching location forecast for: {}", location);
            
            String url = String.format("%s/forecast.json?key=%s&q=%s&days=7&aqi=no&alerts=no",
                    apiUrl, apiKey, location);
            
            String response = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            
            return extractWeatherData(response, location);
            
        } catch (Exception e) {
            logger.error("Error fetching weather data for location: {}", location, e);
            throw new RuntimeException("Failed to fetch weather data for " + location, e);
        }
    }
    
    /**
     * Extracts and structures weather data from API response
     */
    private WeatherData extractWeatherData(String jsonResponse, String region) {
        try {
            JsonNode root = objectMapper.readTree(jsonResponse);
            JsonNode forecastDays = root.path("forecast").path("forecastday");
            
            List<DailyForecast> dailyForecasts = new ArrayList<>();
            double totalRainfall = 0.0;
            double totalTemp = 0.0;
            double maxTemp = Double.MIN_VALUE;
            double minTemp = Double.MAX_VALUE;
            
            for (JsonNode day : forecastDays) {
                JsonNode dayData = day.path("day");
                
                DailyForecast forecast = new DailyForecast();
                forecast.setDate(day.path("date").asText());
                forecast.setMaxTemp(dayData.path("maxtemp_c").asDouble());
                forecast.setMinTemp(dayData.path("mintemp_c").asDouble());
                forecast.setAvgTemp(dayData.path("avgtemp_c").asDouble());
                forecast.setTotalRainfall(dayData.path("totalprecip_mm").asDouble());
                forecast.setChanceOfRain(dayData.path("daily_chance_of_rain").asInt());
                forecast.setCondition(dayData.path("condition").path("text").asText());
                forecast.setHumidity(dayData.path("avghumidity").asDouble());
                forecast.setWindSpeed(dayData.path("maxwind_kph").asDouble());
                
                dailyForecasts.add(forecast);
                
                totalRainfall += forecast.getTotalRainfall();
                totalTemp += forecast.getAvgTemp();
                maxTemp = Math.max(maxTemp, forecast.getMaxTemp());
                minTemp = Math.min(minTemp, forecast.getMinTemp());
            }
            
            WeatherData weatherData = new WeatherData();
            weatherData.setRegion(region);
            weatherData.setDailyForecasts(dailyForecasts);
            weatherData.setTotalRainfall(totalRainfall);
            weatherData.setAvgTemperature(totalTemp / dailyForecasts.size());
            weatherData.setMaxTemperature(maxTemp);
            weatherData.setMinTemperature(minTemp);
            
            logger.info("Successfully extracted weather data for {}: {} days, total rainfall: {}mm",
                    region, dailyForecasts.size(), totalRainfall);
            
            return weatherData;
            
        } catch (Exception e) {
            logger.error("Error parsing weather data", e);
            throw new RuntimeException("Failed to parse weather data", e);
        }
    }
}
