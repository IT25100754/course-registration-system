package com.example.courseregistrationsystem.controller;

import com.example.courseregistrationsystem.dto.LoginRequest;
import com.example.courseregistrationsystem.service.AdminAuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminAuthController {

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {

        String user = request.getUsername();
        String pass = request.getPassword();

        System.out.println("Login Attempt: " + user + " / " + pass);

        // HARDCODED CHECK
        if ("admin@harvard.edu".equals(user) && "admin123".equals(pass)) {
            HttpSession session = httpRequest.getSession(true);
            session.setAttribute("role", "ADMIN");
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession(false);
        if (session != null) session.invalidate();
        return ResponseEntity.ok("Logged out");
    }
}


//    private final AdminAuthService adminAuthService;
//
//    public AdminAuthController(AdminAuthService adminAuthService) {
//        this.adminAuthService = adminAuthService;
//    }
//    @PostMapping("/login")
//    public ResponseEntity<String> login(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {
//
//        System.out.println("Request:"+ request.getUsername() + ", "+request.getPassword() );
//
//        boolean isValidUser = adminAuthService.authenticate(request.username, request.password);
//
//        if (isValidUser) {
//            HttpSession session = httpRequest.getSession(true);
//
//            session.setAttribute("role", "ADMIN");
//            session.setAttribute("username", request.username);
//
//            return ResponseEntity.ok("Login successful");
//        } else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
//        }
//    }
//
//
//    @PostMapping("/logout")
//    public ResponseEntity<String> logout(HttpServletRequest httpRequest) {
//        HttpSession session = httpRequest.getSession(false);
//        if (session != null) {
//            session.invalidate();
//        }
//        return ResponseEntity.ok("Logged out successfully");
//    }
//
//    public boolean authenticate(String username, String password) {
//
//        return "admin@harvard.edu".equals(username) && "admin123".equals(password);
//    }
//




