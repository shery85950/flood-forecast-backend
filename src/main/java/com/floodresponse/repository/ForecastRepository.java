package com.floodresponse.repository;

import com.floodresponse.model.Forecast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ForecastRepository extends JpaRepository<Forecast, Long> {
    List<Forecast> findByRegion(String region);
    List<Forecast> findTop10ByOrderByForecastDateDesc();
}
