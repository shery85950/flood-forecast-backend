package com.floodresponse.service;

import com.floodresponse.model.Forecast;
import com.floodresponse.repository.ForecastRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ForecastService {

    @Autowired
    private ForecastRepository forecastRepository;

    public List<Forecast> getAllForecasts() {
        return forecastRepository.findAll();
    }

    public List<Forecast> getLatestForecasts() {
        return forecastRepository.findTop10ByOrderByForecastDateDesc();
    }

    public Optional<Forecast> getForecastById(Long id) {
        return forecastRepository.findById(id);
    }

    public Forecast createForecast(Forecast forecast) {
        return forecastRepository.save(forecast);
    }

    public Forecast updateForecast(Long id, Forecast forecastDetails) {
        Forecast forecast = forecastRepository.findById(id).orElseThrow(() -> new RuntimeException("Forecast not found"));
        forecast.setRegion(forecastDetails.getRegion());
        forecast.setRiskLevel(forecastDetails.getRiskLevel());
        forecast.setRainfall(forecastDetails.getRainfall());
        forecast.setRiverLevel(forecastDetails.getRiverLevel());
        forecast.setForecastDate(forecastDetails.getForecastDate());
        forecast.setDescription(forecastDetails.getDescription());
        return forecastRepository.save(forecast);
    }

    public void deleteForecast(Long id) {
        forecastRepository.deleteById(id);
    }
}
