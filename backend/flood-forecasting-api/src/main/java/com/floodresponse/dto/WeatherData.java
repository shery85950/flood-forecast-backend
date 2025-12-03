package com.floodresponse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherData {
    private String region;
    private List<DailyForecast> dailyForecasts;
    private double totalRainfall;
    private double avgTemperature;
    private double maxTemperature;
    private double minTemperature;
}
