package com.floodresponse.service;

import com.floodresponse.model.Shelter;
import com.floodresponse.repository.ShelterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ShelterService {

    @Autowired
    private ShelterRepository shelterRepository;

    public List<Shelter> getAllShelters() {
        return shelterRepository.findAll();
    }

    public Optional<Shelter> getShelterById(Long id) {
        return shelterRepository.findById(id);
    }

    public Shelter createShelter(Shelter shelter) {
        return shelterRepository.save(shelter);
    }

    public Shelter updateShelter(Long id, Shelter shelterDetails) {
        Shelter shelter = shelterRepository.findById(id).orElseThrow(() -> new RuntimeException("Shelter not found"));
        shelter.setName(shelterDetails.getName());
        shelter.setAddress(shelterDetails.getAddress());
        shelter.setLatitude(shelterDetails.getLatitude());
        shelter.setLongitude(shelterDetails.getLongitude());
        shelter.setCapacity(shelterDetails.getCapacity());
        shelter.setCurrentOccupancy(shelterDetails.getCurrentOccupancy());
        shelter.setContactNumber(shelterDetails.getContactNumber());
        shelter.setFacilities(shelterDetails.getFacilities());
        shelter.setOpen(shelterDetails.isOpen());
        return shelterRepository.save(shelter);
    }

    public void deleteShelter(Long id) {
        shelterRepository.deleteById(id);
    }

    public List<Shelter> getNearbyShelters(double lat, double lng, double radiusKm) {
        List<Shelter> allShelters = shelterRepository.findByIsOpenTrue();
        return allShelters.stream()
                .filter(shelter -> calculateDistance(lat, lng, shelter.getLatitude(), shelter.getLongitude()) <= radiusKm)
                .collect(Collectors.toList());
    }

    // Haversine formula to calculate distance in km
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the earth in km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
