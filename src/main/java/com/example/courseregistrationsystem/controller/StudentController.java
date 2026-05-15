package com.example.courseregistrationsystem.controller;

import com.example.courseregistrationsystem.model.Student;
import com.example.courseregistrationsystem.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * StudentController — REST API for student operations.
 *
 * Endpoints match exactly what the frontend JavaScript calls:
 *   GET    /api/students
 *   POST   /api/students
 *   POST   /api/students/login
 *   PUT    /api/students/{studentId}/phone
 *   PUT    /api/students/{studentId}/password
 *   POST   /api/students/reset-password
 *   DELETE /api/students/{studentId}
 */
@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // ─── READ: Get all students ───────────────────────────────────────────────────

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllStudents() {
        List<Map<String, Object>> students = studentService.getAllStudents()
                .stream()
                .map(studentService::toSafeMap)
                .collect(Collectors.toList());
        return ResponseEntity.ok(students);
    }

    // ─── READ: Get single student ────────────────────────────────────────────────

    @GetMapping("/{studentId}")
    public ResponseEntity<?> getStudent(@PathVariable String studentId) {
        return studentService.getByStudentId(studentId)
                .map(s -> ResponseEntity.ok(studentService.toSafeMap(s)))
                .orElse(ResponseEntity.notFound().build());
    }

    // ─── CREATE: Register new student ────────────────────────────────────────────

    @PostMapping
    public ResponseEntity<?> registerStudent(@RequestBody Map<String, String> body) {
        try {
            Student student = new Student();
            student.setName(body.get("name"));
            student.setEmail(body.get("email"));
            student.setPassword(body.get("password"));
            student.setStudentId(body.get("studentId"));
            student.setPhone(body.getOrDefault("phone", "Not provided"));
            student.setType(body.getOrDefault("type", "undergraduate"));

            Student saved = studentService.register(student);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(studentService.toSafeMap(saved));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", e.getMessage()));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Registration failed: " + e.getMessage()));
        }
    }

    // ─── LOGIN ───────────────────────────────────────────────────────────────────

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String email    = body.get("email");
        String password = body.get("password");

        return studentService.login(email, password)
                .map(s -> ResponseEntity.ok(studentService.toSafeMap(s)))
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Invalid email or password")));
    }

    // ─── UPDATE: Change phone ─────────────────────────────────────────────────────

    @PutMapping("/{studentId}/phone")
    public ResponseEntity<?> updatePhone(@PathVariable String studentId,
                                         @RequestBody Map<String, String> body) {
        try {
            Student updated = studentService.updatePhone(studentId, body.get("phone"));
            return ResponseEntity.ok(studentService.toSafeMap(updated));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // ─── UPDATE: Change password ──────────────────────────────────────────────────

    @PutMapping("/{studentId}/password")
    public ResponseEntity<?> changePassword(@PathVariable String studentId,
                                            @RequestBody Map<String, String> body) {
        try {
            studentService.updatePassword(studentId, body.get("currentPassword"), body.get("newPassword"));
            return ResponseEntity.ok(Map.of("message", "Password changed successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // ─── UPDATE: Reset password ───────────────────────────────────────────────────

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> body) {
        try {
            studentService.resetPassword(body.get("email"), body.get("newPassword"));
            return ResponseEntity.ok(Map.of("message", "Password reset successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // ─── DELETE ──────────────────────────────────────────────────────────────────

    @DeleteMapping("/{studentId}")
    public ResponseEntity<?> deleteStudent(@PathVariable String studentId) {
        try {
            boolean deleted = studentService.deleteStudent(studentId);
            if (deleted) {
                return ResponseEntity.ok(Map.of("message", "Student deleted"));
            }
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
