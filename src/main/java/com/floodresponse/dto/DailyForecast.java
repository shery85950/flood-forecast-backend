package com.floodresponse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyForecast {
    private String date;
    private double maxTemp;
    private double minTemp;
    private double avgTemp;
    private double totalRainfall;
    private int chanceOfRain;
    private String condition;
    private double humidity;
    private double windSpeed;
}
