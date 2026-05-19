package com.example.courseregistrationsystem.controller;

import com.example.courseregistrationsystem.dto.LoginRequest;
import com.example.courseregistrationsystem.service.AdminAuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/auth")
public class AdminAuthController {

    private final AdminAuthService adminAuthService;

    public AdminAuthController(AdminAuthService adminAuthService) {
        this.adminAuthService = adminAuthService;
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {

        System.out.println("Request:"+ request.getUsername() + ", "+request.getPassword() );

        boolean isValidUser = adminAuthService.authenticate(request.username, request.password);

        Map<String, Object> responseBody = new HashMap<>();

        if (isValidUser) {
            HttpSession session = httpRequest.getSession(true);

            session.setAttribute("role", "ADMIN");
            session.setAttribute("username", request.username);

            //Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("success", true);
            responseBody.put("message", "Login successful");

            return ResponseEntity.ok(responseBody);


        } else {
            //Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("success", false);
            responseBody.put("message", "Invalid credentials");

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
        }


    }


    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return ResponseEntity.ok("Logged out successfully");
    }

    public boolean authenticate(String username, String password) {

        return "admin@harvard.edu".equals(username) && "admin123".equals(password);
    }}





