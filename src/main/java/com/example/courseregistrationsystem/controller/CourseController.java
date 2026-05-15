package com.example.courseregistrationsystem.controller;

import com.example.courseregistrationsystem.model.Course;
import com.example.courseregistrationsystem.service.RegistrationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * CourseController — REST endpoints for courses.
 *
 * GET    /api/courses           — getAllCourse() from UML CourseManager
 * POST   /api/courses           — addCourse() from UML CourseManager
 * PUT    /api/courses/{id}      — update course
 * DELETE /api/courses/{id}      — removeCourse() from UML CourseManager
 * GET    /api/courses/available/{studentId} — available courses for a student
 */
@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "*")
public class CourseController {

    private final RegistrationService registrationService;

    public CourseController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        return ResponseEntity.ok(registrationService.getAllCourses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCourse(@PathVariable String id) {
        return registrationService.getCourseById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/available/{studentId}")
    public ResponseEntity<List<Map<String, Object>>> getAvailableCourses(
            @PathVariable String studentId) {
        return ResponseEntity.ok(
                registrationService.getAvailableCoursesForStudent(studentId));
    }

    @PostMapping
    public ResponseEntity<?> addCourse(@RequestBody Course course) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(registrationService.addCourse(course));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable String id,
                                          @RequestBody Course course) {
        try {
            course.setId(id);
            return ResponseEntity.ok(registrationService.updateCourse(course));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable String id) {
        try {
            boolean deleted = registrationService.removeCourse(id);
            return deleted
                    ? ResponseEntity.ok(Map.of("message", "Course deleted"))
                    : ResponseEntity.notFound().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
