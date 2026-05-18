package com.example.courseregistrationsystem.controller;

import com.example.courseregistrationsystem.model.Student;
import com.example.courseregistrationsystem.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@CrossOrigin(origins = "*")
public class ProfileController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/{studentId}")
    public ResponseEntity<Student> getProfile(@PathVariable String studentId) {

        Student student = studentService.getProfile(studentId);

        if (student == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(student);
    }

    @PutMapping("/update-phone")
    public ResponseEntity<String> updatePhone(
            @RequestParam String studentId,
            @RequestParam String phone) {

        boolean updated = studentService.updatePhone(studentId, phone);

        return updated
                ? ResponseEntity.ok("Phone updated")
                : ResponseEntity.badRequest().body("Student not found");
    }

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @RequestParam String studentId,
            @RequestParam String newPassword) {

        boolean updated = studentService.changePassword(studentId, newPassword);

        return updated
                ? ResponseEntity.ok("Password changed")
                : ResponseEntity.badRequest().body("Student not found");
    }
}