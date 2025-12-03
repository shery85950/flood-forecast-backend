package com.floodresponse.seeder;

import com.floodresponse.model.*;
import com.floodresponse.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private AlertRepository alertRepository;
    @Autowired
    private ShelterRepository shelterRepository;
    @Autowired
    private HelplineRepository helplineRepository;
    @Autowired
    private ForecastRepository forecastRepository;
    @Autowired
    private AdminRepository adminRepository;

    @Override
    public void run(String... args) throws Exception {
        if (alertRepository.count() == 0) {
            seedAlerts();
            seedShelters();
            seedHelplines();
            seedForecasts();
            seedAdmins();
        }
    }

    private void seedAlerts() {
        Alert alert1 = new Alert();
        alert1.setTitle("High Flood Warning - River Indus");
        alert1.setDescription("Water levels at Guddu Barrage have reached critical levels. Residents in low-lying areas are advised to evacuate immediately.");
        alert1.setSeverity("Critical");
        alert1.setRegion("Sindh");
        alert1.setExpiresAt(LocalDateTime.now().plusDays(2));
        alertRepository.save(alert1);

        Alert alert2 = new Alert();
        alert2.setTitle("Heavy Rainfall Alert");
        alert2.setDescription("Heavy monsoon rains expected in Lahore and surrounding areas for the next 48 hours.");
        alert2.setSeverity("Warning");
        alert2.setRegion("Punjab");
        alert2.setExpiresAt(LocalDateTime.now().plusDays(1));
        alertRepository.save(alert2);
    }

    private void seedShelters() {
        Shelter shelter1 = new Shelter();
        shelter1.setName("Government High School Model Town");
        shelter1.setAddress("Model Town, Lahore");
        shelter1.setLatitude(31.4805);
        shelter1.setLongitude(74.3239);
        shelter1.setCapacity(500);
        shelter1.setCurrentOccupancy(120);
        shelter1.setContactNumber("0300-1234567");
        shelter1.setFacilities("Food, Water, Medical Aid");
        shelter1.setOpen(true);
        shelterRepository.save(shelter1);

        Shelter shelter2 = new Shelter();
        shelter2.setName("Community Center Gulberg");
        shelter2.setAddress("Gulberg III, Lahore");
        shelter2.setLatitude(31.5204);
        shelter2.setLongitude(74.3587);
        shelter2.setCapacity(300);
        shelter2.setCurrentOccupancy(50);
        shelter2.setContactNumber("0321-7654321");
        shelter2.setFacilities("Food, Water");
        shelter2.setOpen(true);
        shelterRepository.save(shelter2);
    }

    private void seedHelplines() {
        Helpline h1 = new Helpline();
        h1.setName("Rescue 1122");
        h1.setPhoneNumber("1122");
        h1.setCategory("Rescue");
        h1.setRegion("Nationwide");
        h1.setDescription("Emergency Ambulance and Rescue Service");
        helplineRepository.save(h1);

        Helpline h2 = new Helpline();
        h2.setName("PDMA Helpline");
        h2.setPhoneNumber("1129");
        h2.setCategory("Government");
        h2.setRegion("Provincial");
        h2.setDescription("Provincial Disaster Management Authority");
        helplineRepository.save(h2);
    }

    private void seedForecasts() {
        Forecast f1 = new Forecast();
        f1.setRegion("River Indus");
        f1.setRiskLevel("High");
        f1.setRainfall(50.5);
        f1.setRiverLevel("High Flood");
        f1.setForecastDate(LocalDateTime.now().plusHours(24));
        f1.setDescription("Water levels rising due to heavy upstream rainfall.");
        forecastRepository.save(f1);

        Forecast f2 = new Forecast();
        f2.setRegion("River Ravi");
        f2.setRiskLevel("Low");
        f2.setRainfall(5.0);
        f2.setRiverLevel("Normal");
        f2.setForecastDate(LocalDateTime.now().plusHours(24));
        f2.setDescription("Normal flow conditions expected.");
        forecastRepository.save(f2);
    }

    private void seedAdmins() {
        Admin admin = new Admin();
        admin.setUsername("admin");
        admin.setPassword("admin123"); // In production, use BCrypt
        admin.setEmail("admin@floodresponse.org");
        admin.setRole("SUPER_ADMIN");
        adminRepository.save(admin);
    }
}
