package com.floodresponse.service;

import com.floodresponse.model.Helpline;
import com.floodresponse.repository.HelplineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HelplineService {

    @Autowired
    private HelplineRepository helplineRepository;

    public List<Helpline> getAllHelplines() {
        return helplineRepository.findAll();
    }

    public List<Helpline> getHelplinesByCategory(String category) {
        return helplineRepository.findByCategory(category);
    }

    public Optional<Helpline> getHelplineById(Long id) {
        return helplineRepository.findById(id);
    }

    public Helpline createHelpline(Helpline helpline) {
        return helplineRepository.save(helpline);
    }

    public Helpline updateHelpline(Long id, Helpline helplineDetails) {
        Helpline helpline = helplineRepository.findById(id).orElseThrow(() -> new RuntimeException("Helpline not found"));
        helpline.setName(helplineDetails.getName());
        helpline.setPhoneNumber(helplineDetails.getPhoneNumber());
        helpline.setCategory(helplineDetails.getCategory());
        helpline.setRegion(helplineDetails.getRegion());
        helpline.setDescription(helplineDetails.getDescription());
        return helplineRepository.save(helpline);
    }

    public void deleteHelpline(Long id) {
        helplineRepository.deleteById(id);
    }
}
