package com.example.courseregistrationsystem.controller;

import com.example.courseregistrationsystem.model.Student;
import com.example.courseregistrationsystem.service.AdminService;
import com.example.courseregistrationsystem.service.StudentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/students")
public class AdminStudentController {


    private final AdminService adminService;

    public AdminStudentController(AdminService adminService) {
        this.adminService = adminService;
    }

    private boolean isAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            Object role = session.getAttribute("role");
            System.out.println("Session ID: " + session.getId() + " | Role: " + role);
            return "ADMIN".equals(role);
        }

        System.out.println("No session found for this request.");
        return false;
    }

    @GetMapping
    public ResponseEntity<?> getStudents(HttpServletRequest request) {
        if (!isAdmin(request)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        return ResponseEntity.ok(adminService.getAllStudents());
    }

    @PostMapping
    public ResponseEntity<String> saveStudent(@RequestBody Student student, HttpServletRequest request) {
        if (!isAdmin(request)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        adminService.addStudent(student);
        return ResponseEntity.ok("Student saved");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable String id, HttpServletRequest request) {
        if (!isAdmin(request)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        adminService.deleteStudent(id);
        return ResponseEntity.ok("Student deleted");
    }

}
