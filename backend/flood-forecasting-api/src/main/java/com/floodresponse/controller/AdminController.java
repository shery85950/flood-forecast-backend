package com.floodresponse.controller;

import com.floodresponse.model.Admin;
import com.floodresponse.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");
        
        return adminService.login(username, password)
                .map(admin -> ResponseEntity.ok().body(Map.of("message", "Login successful", "role", admin.getRole())))
                .orElse(ResponseEntity.status(401).body(Map.of("message", "Invalid credentials")));
    }
}
