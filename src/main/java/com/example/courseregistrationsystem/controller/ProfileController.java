package com.example.courseregistrationsystem.controller;

import com.example.courseregistrationsystem.model.StudentDTO;
import com.example.courseregistrationsystem.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

    @RestController
    @RequestMapping("/api/profile")
    @CrossOrigin(origins = "*")
    public class ProfileController {

        private final StudentService studentService;

        @Autowired
        public ProfileController(StudentService studentService) {
            this.studentService = studentService;
        }

        @GetMapping("/{studentId}")
        public ResponseEntity<StudentDTO.ProfileResponse> getProfile(
                @PathVariable String studentId) {

            StudentDTO.ProfileResponse profile = studentService.getProfile(studentId);
            if (profile.isSuccess()) {
                return ResponseEntity.ok(profile);
            } else {
                return ResponseEntity.status(404).body(profile);
            }
        }

        @PutMapping("/update-phone")
        public ResponseEntity<StudentDTO.ApiResponse> updatePhone(
                @RequestBody StudentDTO.UpdatePhoneRequest request) {

            StudentDTO.ApiResponse response = studentService.updatePhone(request);
            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(400).body(response);
            }
        }

        @PutMapping("/change-password")
        public ResponseEntity<StudentDTO.ApiResponse> changePassword(
                @RequestBody StudentDTO.ChangePasswordRequest request) {

            StudentDTO.ApiResponse response = studentService.changePassword(request);
            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(400).body(response);
            }
        }
    }

