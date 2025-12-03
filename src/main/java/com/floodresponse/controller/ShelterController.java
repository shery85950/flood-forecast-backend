package com.floodresponse.controller;

import com.floodresponse.model.Shelter;
import com.floodresponse.service.ShelterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shelters")
public class ShelterController {

    @Autowired
    private ShelterService shelterService;

    @GetMapping
    public List<Shelter> getAllShelters() {
        return shelterService.getAllShelters();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Shelter> getShelterById(@PathVariable Long id) {
        return shelterService.getShelterById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/nearby")
    public List<Shelter> getNearbyShelters(@RequestParam double lat, @RequestParam double lng) {
        // Default radius 50km
        return shelterService.getNearbyShelters(lat, lng, 50.0);
    }

    @PostMapping
    public Shelter createShelter(@RequestBody Shelter shelter) {
        return shelterService.createShelter(shelter);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Shelter> updateShelter(@PathVariable Long id, @RequestBody Shelter shelter) {
        try {
            return ResponseEntity.ok(shelterService.updateShelter(id, shelter));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShelter(@PathVariable Long id) {
        shelterService.deleteShelter(id);
        return ResponseEntity.ok().build();
    }
}
