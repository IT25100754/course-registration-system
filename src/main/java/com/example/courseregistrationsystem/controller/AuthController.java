package com.example.courseregistrationsystem.controller;

import com.example.courseregistrationsystem.model.StudentDTO;
import com.example.courseregistrationsystem.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final StudentService studentService;

    @Autowired
    public AuthController(StudentService studentService) {
        this.studentService = studentService;
    }

    // GET - shows API info when opened in browser
    @GetMapping("/login")
    public ResponseEntity<Map<String, String>> loginInfo() {
        Map<String, String> info = new HashMap<>();
        info.put("endpoint", "POST /api/auth/login");
        info.put("description", "Login endpoint - send POST request with email and password");
        info.put("requiredFields", "email, password");
        info.put("example", "{\"email\": \"alex@harvard.edu\", \"password\": \"pass123\"}");
        return ResponseEntity.ok(info);
    }

    // POST - actual login used by frontend
    @PostMapping("/login")
    public ResponseEntity<StudentDTO.ApiResponse> login(
            @RequestBody StudentDTO.LoginRequest request) {

        StudentDTO.ApiResponse response = studentService.login(request);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body(response);
        }
    }

    // GET - shows API info when opened in browser
    @GetMapping("/register")
    public ResponseEntity<Map<String, String>> registerInfo() {
        Map<String, String> info = new HashMap<>();
        info.put("endpoint", "POST /api/auth/register");
        info.put("description", "Register endpoint - send POST request with student details");
        info.put("requiredFields", "name, email, password, studentId, phone");
        info.put("example", "{\"name\": \"John\", \"email\": \"john@harvard.edu\", \"password\": \"pass123\", \"studentId\": \"HARVARD003\", \"phone\": \"+1 234 567 8900\"}");
        return ResponseEntity.ok(info);
    }

    // POST - actual register used by frontend
    @PostMapping("/register")
    public ResponseEntity<StudentDTO.ApiResponse> register(
            @RequestBody StudentDTO.RegisterRequest request) {

        StudentDTO.ApiResponse response = studentService.register(request);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(400).body(response);
        }
    }

    // GET - shows API info when opened in browser
    @GetMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPasswordInfo() {
        Map<String, String> info = new HashMap<>();
        info.put("endpoint", "POST /api/auth/reset-password");
        info.put("description", "Reset password endpoint - send POST request with email and new password");
        info.put("requiredFields", "email, newPassword, confirmPassword");
        info.put("example", "{\"email\": \"alex@harvard.edu\", \"newPassword\": \"newpass123\", \"confirmPassword\": \"newpass123\"}");
        return ResponseEntity.ok(info);
    }

    // POST - actual reset password used by frontend
    @PostMapping("/reset-password")
    public ResponseEntity<StudentDTO.ApiResponse> resetPassword(
            @RequestBody StudentDTO.ResetPasswordRequest request) {

        StudentDTO.ApiResponse response = studentService.resetPassword(request);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(400).body(response);
        }
    }
}