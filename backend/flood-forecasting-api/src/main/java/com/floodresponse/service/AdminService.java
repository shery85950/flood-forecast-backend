package com.floodresponse.service;

import com.floodresponse.model.Admin;
import com.floodresponse.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    public Optional<Admin> login(String username, String password) {
        Optional<Admin> admin = adminRepository.findByUsername(username);
        if (admin.isPresent() && admin.get().getPassword().equals(password)) {
            return admin;
        }
        return Optional.empty();
    }

    public Admin createAdmin(Admin admin) {
        return adminRepository.save(admin);
    }
}
