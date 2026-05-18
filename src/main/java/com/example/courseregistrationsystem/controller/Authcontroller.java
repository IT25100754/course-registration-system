package com.example.courseregistrationsystem.controller;

import com.example.courseregistrationsystem.dto.StudentDTO;
import com.example.courseregistrationsystem.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

    @RestController
    @RequestMapping("/api/auth")
    @CrossOrigin(origins = "*")
    public class Authcontroller {

        private final StudentService studentService;

        @Autowired
        public Authcontroller(StudentService studentService) {
            this.studentService = studentService;
        }

        @PostMapping("/login")
        public ResponseEntity<StudentDTO.ApiResponse> login(
                @RequestBody StudentDTO.LoginRequest request) {

            StudentDTO.ApiResponse response = studentService.login(request);

            return response.isSuccess()
                    ? ResponseEntity.ok(response)
                    : ResponseEntity.status(401).body(response);
        }

        @PostMapping("/register")
        public ResponseEntity<StudentDTO.ApiResponse> register(
                @RequestBody StudentDTO.RegisterRequest request) {

            StudentDTO.ApiResponse response = studentService.register(request);

            return response.isSuccess()
                    ? ResponseEntity.ok(response)
                    : ResponseEntity.status(400).body(response);
        }

        @PostMapping("/reset-password")
        public ResponseEntity<StudentDTO.ApiResponse> resetPassword(
                @RequestBody StudentDTO.ResetPasswordRequest request) {

            StudentDTO.ApiResponse response = studentService.resetPassword(request);

            return response.isSuccess()
                    ? ResponseEntity.ok(response)
                    : ResponseEntity.status(400).body(response);
        }
    }

