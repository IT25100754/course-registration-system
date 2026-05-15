package com.example.courseregistrationsystem.controller;

import com.example.courseregistrationsystem.model.Registration;
import com.example.courseregistrationsystem.service.RegistrationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * RegistrationController — REST endpoints for course registrations.
 *
 * GET    /api/registrations               — all registrations
 * POST   /api/registrations               — registerStudent() from UML
 * GET    /api/registrations/student/{id}  — getStudentCourse() from UML
 * GET    /api/registrations/roster/{id}   — course roster (admin view)
 * PUT    /api/registrations/section       — update section (Edit Registration)
 * DELETE /api/registrations               — cancelRegistration() from UML
 */
@RestController
@RequestMapping("/api/registrations")
@CrossOrigin(origins = "*")
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    // ─── READ: All registrations ──────────────────────────────────────────────────

    @GetMapping
    public ResponseEntity<List<Registration>> getAllRegistrations() {
        return ResponseEntity.ok(registrationService.getAllRegistrations());
    }

    // ─── READ: Student's registrations ───────────────────────────────────────────

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Registration>> getStudentRegistrations(
            @PathVariable String studentId) {
        return ResponseEntity.ok(registrationService.getStudentRegistrations(studentId));
    }

    // ─── READ: Course Roster ──────────────────────────────────────────────────────

    @GetMapping("/roster/{courseId}")
    public ResponseEntity<?> getCourseRoster(@PathVariable String courseId) {
        try {
            List<Map<String, Object>> roster = registrationService.getCourseRoster(courseId);
            return ResponseEntity.ok(roster.get(0)); // return the single map
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // ─── CREATE: Register student for a course ────────────────────────────────────

    @PostMapping
    public ResponseEntity<?> createRegistration(@RequestBody Map<String, String> body) {
        try {
            String studentId   = body.get("studentId");
            String courseId    = body.get("courseId");
            String studentType = body.getOrDefault("studentType", "undergraduate");

            Registration reg = registrationService.createRegistration(
                    studentId, courseId, studentType);
            return ResponseEntity.status(HttpStatus.CREATED).body(reg);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // ─── UPDATE: Change section ───────────────────────────────────────────────────

    @PutMapping("/section")
    public ResponseEntity<?> updateSection(@RequestBody Map<String, String> body) {
        try {
            String studentId    = body.get("studentId");
            String courseId     = body.get("courseId");
            String newSectionId = body.get("newSectionId");

            Registration updated = registrationService.updateSection(
                    studentId, courseId, newSectionId);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // ─── DELETE: Cancel registration ──────────────────────────────────────────────

    @DeleteMapping
    public ResponseEntity<?> cancelRegistration(@RequestBody Map<String, String> body) {
        try {
            String studentId = body.get("studentId");
            String courseId  = body.get("courseId");

            registrationService.cancelRegistration(studentId, courseId);
            return ResponseEntity.ok(Map.of("message", "Registration cancelled successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // ─── Grade endpoints ──────────────────────────────────────────────────────────

    @GetMapping("/grades")
    public ResponseEntity<?> getAllGrades() {
        return ResponseEntity.ok(registrationService.getAllGrades());
    }

    @GetMapping("/grades/student/{studentId}")
    public ResponseEntity<?> getStudentGrades(@PathVariable String studentId) {
        return ResponseEntity.ok(registrationService.getGradesByStudentId(studentId));
    }

    @PostMapping("/grades")
    public ResponseEntity<?> addGrade(@RequestBody Map<String, Object> body) {
        try {
            String studentId = (String) body.get("studentId");
            String courseId  = (String) body.get("courseId");
            double marks     = Double.parseDouble(body.get("marks").toString());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(registrationService.addGrade(studentId, courseId, marks));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
