package com.example.courseregistrationsystem.controller;

import com.example.courseregistrationsystem.dto.StudentDTO;
import com.example.courseregistrationsystem.service.StudentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
// Note: We removed @CrossOrigin because we fixed it globally in WebConfig.java
public class AuthController {

    private final StudentService studentService;

    @Autowired
    public AuthController(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * MERGED LOGIN: Handles both Admin and Student
     */
    @PostMapping("/login")
    public ResponseEntity<StudentDTO.ApiResponse> login(
            @RequestBody StudentDTO.LoginRequest request,
            HttpServletRequest httpRequest) {

        String email = request.getEmail();
        String password = request.getPassword();

        // 1. CHECK IF ADMIN (Hardcoded logic from your AdminAuthController)
        if ("admin@harvard.edu".equals(email) && "admin123".equals(password)) {
            HttpSession session = httpRequest.getSession(true);
            session.setAttribute("role", "ADMIN");

            return ResponseEntity.ok(new StudentDTO.ApiResponse(true, "Admin Login Successful", null));
        }

        // 2. IF NOT ADMIN, CHECK STUDENT SERVICE
        StudentDTO.ApiResponse response = studentService.login(request);

        if (response.isSuccess()) {
            HttpSession session = httpRequest.getSession(true);
            session.setAttribute("role", "STUDENT");
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body(response);
        }
    }

    /**
     * STUDENT REGISTRATION
     */
    @PostMapping("/register")
    public ResponseEntity<StudentDTO.ApiResponse> register(
            @RequestBody StudentDTO.RegisterRequest request) {

        StudentDTO.ApiResponse response = studentService.register(request);

        return response.isSuccess()
                ? ResponseEntity.ok(response)
                : ResponseEntity.status(400).body(response);
    }

    /**
     * PASSWORD RESET
     */
    @PostMapping("/reset-password")
    public ResponseEntity<StudentDTO.ApiResponse> resetPassword(
            @RequestBody StudentDTO.ResetPasswordRequest request) {

        StudentDTO.ApiResponse response = studentService.resetPassword(request);

        return response.isSuccess()
                ? ResponseEntity.ok(response)
                : ResponseEntity.status(400).body(response);
    }

    /**
     * LOGOUT (Unified)
     */
    @PostMapping("/logout")
    public ResponseEntity<StudentDTO.ApiResponse> logout(HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return ResponseEntity.ok(new StudentDTO.ApiResponse(true, "Logged out successfully", null));
    }
}