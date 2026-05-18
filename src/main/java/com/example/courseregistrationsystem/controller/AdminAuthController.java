package com.example.courseregistrationsystem.controller;

import com.example.courseregistrationsystem.dto.LoginRequest;
import com.example.courseregistrationsystem.service.AdminAuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminAuthController {


    private final AdminAuthService adminAuthService;

    public AdminAuthController(AdminAuthService adminAuthService) {
        this.adminAuthService = adminAuthService;
    }
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        System.out.println("Request:"+ request.getUsername() + ", "+request.getPassword() );
        boolean isValidUser = adminAuthService.authenticate(request.username, request.password);
        if (isValidUser) {
            // HOW SESSIONS WORK: Create a session (or get existing one)
            HttpSession session = httpRequest.getSession(true);

            // Save admin identity inside the server's memory for this specific user
            session.setAttribute("role", "ADMIN");
            session.setAttribute("username", request.username);

            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    // 3. LOGOUT ENDPOINT
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession(false); // Get session, don't create a new one
        if (session != null) {
            session.invalidate(); // Destroy the session!
        }
        return ResponseEntity.ok("Logged out successfully");
    }

    public boolean authenticate(String username, String password) {
        // Hardcoded check or check from a file
        return "admin@harvard.edu".equals(username) && "admin123".equals(password);
    }



}

