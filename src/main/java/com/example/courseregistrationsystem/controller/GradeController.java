package com.example.courseregistrationsystem.controller;

import com.example.courseregistrationsystem.model.Grade;
import com.example.courseregistrationsystem.service.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/grades")
public class GradeController {

    private final AdminService adminService;

    public GradeController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping
    public ResponseEntity<?> getGrades(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        return ResponseEntity.ok(adminService.getAllGrades());
    }

    @PostMapping
    public ResponseEntity<String> addGrade(@RequestBody Grade grade, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        adminService.addGrade(grade);
        return ResponseEntity.ok("Grade added successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGrade(@PathVariable String id, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        adminService.deleteGrade(id);
        return ResponseEntity.ok("Grade deleted");
    }
}