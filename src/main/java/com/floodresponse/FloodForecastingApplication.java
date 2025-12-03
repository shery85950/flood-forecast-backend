package com.floodresponse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FloodForecastingApplication {

	public static void main(String[] args) {
		SpringApplication.run(FloodForecastingApplication.class, args);
	}

}
