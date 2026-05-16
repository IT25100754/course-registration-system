package com.example.courseregistrationsystem.controller;


import com.example.courseregistrationsystem.model.Course;
import com.example.courseregistrationsystem.model.Grade;
import com.example.courseregistrationsystem.model.StudentDTO;
import com.example.courseregistrationsystem.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    private final DashboardService dashboardService;

    @Autowired
    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<StudentDTO.DashboardResponse> getDashboard(
            @PathVariable String studentId) {

        StudentDTO.DashboardResponse data = dashboardService.getDashboardData(studentId);
        return ResponseEntity.ok(data);
    }

    @GetMapping("/{studentId}/courses/enrolled")
    public ResponseEntity<List<Course>> getEnrolledCourses(
            @PathVariable String studentId) {

        List<Course> courses = dashboardService.getEnrolledCourses(studentId);
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/{studentId}/courses/available")
    public ResponseEntity<List<Course>> getAvailableCourses(
            @PathVariable String studentId) {

        List<Course> courses = dashboardService.getAvailableCourses(studentId);
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/{studentId}/grades")
    public ResponseEntity<List<Grade>> getGrades(
            @PathVariable String studentId) {

        List<Grade> grades = dashboardService.getGrades(studentId);
        return ResponseEntity.ok(grades);
    }

    @PostMapping("/{studentId}/courses/register")
    public ResponseEntity<StudentDTO.ApiResponse> registerCourse(
            @PathVariable String studentId,
            @RequestBody Map<String, String> body) {

        String courseId = body.get("courseId");
        if (courseId == null || courseId.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(new StudentDTO.ApiResponse(false, "courseId is required"));
        }

        StudentDTO.ApiResponse response = dashboardService.registerCourse(studentId, courseId);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(400).body(response);
        }
    }

    @DeleteMapping("/{studentId}/courses/{courseId}")
    public ResponseEntity<StudentDTO.ApiResponse> unregisterCourse(
            @PathVariable String studentId,
            @PathVariable String courseId) {

        StudentDTO.ApiResponse response = dashboardService.unregisterCourse(studentId, courseId);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(400).body(response);
        }
    }
}

