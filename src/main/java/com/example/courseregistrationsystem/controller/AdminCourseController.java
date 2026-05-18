package com.example.courseregistrationsystem.controller;


import com.example.courseregistrationsystem.model.Course;
import com.example.courseregistrationsystem.service.CourseService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses")

public class AdminCourseController {

    private final CourseService courseService;

    public AdminCourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    // Helper method to check if Admin is logged in
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

    //  GET ALL COURSES
    @GetMapping
    public ResponseEntity<?> getCourses(HttpServletRequest request) {
        if (!isAdmin(request)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in");
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    //  ADD OR UPDATE COURSE
    @PostMapping
    public ResponseEntity<String> saveCourse(@RequestBody Course course, HttpServletRequest request) {
        if (!isAdmin(request)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in");

        courseService.addCourse(course);
        return ResponseEntity.ok("Course saved successfully!");
    }

    //  DELETE COURSE
    @DeleteMapping("/{courseId}")
    public ResponseEntity<String> deleteCourse(@PathVariable String courseId, HttpServletRequest request) {
        if (!isAdmin(request)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in");

        String deleted = courseService.deleteCourse(courseId);
        if (Boolean.parseBoolean(deleted)) {
            return ResponseEntity.ok("Course deleted successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found");
        }
    }
}