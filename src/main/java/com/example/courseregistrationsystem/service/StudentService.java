package com.example.courseregistrationsystem.service;

import com.example.courseregistrationsystem.model.Student;
import com.example.courseregistrationsystem.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * StudentService — business logic layer for students.
 *
 * Implements UML Student methods:
 * - registerCourse()     → delegates to RegistrationService
 * - dropCourse()         → delegates to RegistrationService
 * - viewRegisteredCourse() → delegates to RegistrationService
 *
 * OOP: Demonstrates the SERVICE layer in the layered architecture,
 * keeping business logic separate from persistence (repository).
 */
@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // ─── CREATE ─────────────────────────────────────────────────────────────────

    public Student register(Student student) throws IOException {
        if (studentRepository.existsByEmail(student.getEmail())) {
            throw new IllegalArgumentException("Email already registered: " + student.getEmail());
        }
        return studentRepository.save(student);
    }

    // ─── READ ────────────────────────────────────────────────────────────────────

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Optional<Student> getByStudentId(String studentId) {
        return studentRepository.findByStudentId(studentId);
    }

    public Optional<Student> getById(String id) {
        return studentRepository.findById(id);
    }

    // ─── LOGIN ───────────────────────────────────────────────────────────────────

    public Optional<Student> login(String email, String password) {
        return studentRepository.findByEmail(email)
                .filter(s -> password.equals(s.getPassword()));
    }

    // ─── UPDATE ──────────────────────────────────────────────────────────────────

    public Student updatePhone(String studentId, String phone) throws IOException {
        Student student = studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found: " + studentId));
        student.setPhone(phone);
        return studentRepository.update(student);
    }

    public Student updatePassword(String studentId, String oldPwd, String newPwd) throws IOException {
        Student student = studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found: " + studentId));
        if (!oldPwd.equals(student.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }
        student.setPassword(newPwd);
        return studentRepository.update(student);
    }

    public Student resetPassword(String email, String newPwd) throws IOException {
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email not found: " + email));
        student.setPassword(newPwd);
        return studentRepository.update(student);
    }

    // ─── DELETE ──────────────────────────────────────────────────────────────────

    public boolean deleteStudent(String studentId) throws IOException {
        Student student = studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found: " + studentId));
        return studentRepository.deleteById(student.getId());
    }

    // ─── SAFE response (strip password) ─────────────────────────────────────────

    public Map<String, Object> toSafeMap(Student s) {
        return Map.of(
                "id",        s.getId(),
                "studentId", s.getStudentId(),
                "name",      s.getName(),
                "email",     s.getEmail(),
                "phone",     s.getPhone() != null ? s.getPhone() : "Not provided",
                "type",      s.getType() != null ? s.getType() : "undergraduate"
        );
    }
}
