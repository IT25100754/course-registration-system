package com.example.courseregistrationsystem.controller;

import com.example.courseregistrationsystem.dto.LoginRequest;
import com.example.courseregistrationsystem.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public boolean login(@RequestBody LoginRequest request) {

        return authService.login(
                request.getEmail(),
                request.getPassword()
        );
    }
}
