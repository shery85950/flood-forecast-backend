package com.floodresponse.controller;

import com.floodresponse.model.Helpline;
import com.floodresponse.service.HelplineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/helplines")
public class HelplineController {

    @Autowired
    private HelplineService helplineService;

    @GetMapping
    public List<Helpline> getAllHelplines() {
        return helplineService.getAllHelplines();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Helpline> getHelplineById(@PathVariable Long id) {
        return helplineService.getHelplineById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Helpline createHelpline(@RequestBody Helpline helpline) {
        return helplineService.createHelpline(helpline);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Helpline> updateHelpline(@PathVariable Long id, @RequestBody Helpline helpline) {
        try {
            return ResponseEntity.ok(helplineService.updateHelpline(id, helpline));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHelpline(@PathVariable Long id) {
        helplineService.deleteHelpline(id);
        return ResponseEntity.ok().build();
    }
}
