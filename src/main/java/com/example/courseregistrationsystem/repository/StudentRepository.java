package com.example.courseregistrationsystem.repository;

import com.example.courseregistrationsystem.model.Student;
import com.example.courseregistrationsystem.util.FileHandler;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * StudentRepository — implements all CRUD operations for Student using file I/O.
 *
 * OOP Concepts:
 * - INHERITANCE: Implements FileRepository<Student, String>
 * - ENCAPSULATION: FileHandler dependency is private
 * - POLYMORPHISM: Each repository has its own entity parsing logic
 *
 * Demonstrates the UML RegistrationManager.registerStudent() / dropStudent()
 * equivalents at the data layer.
 */
@Repository
public class StudentRepository implements FileRepository<Student, String> {

    private final FileHandler fileHandler;

    public StudentRepository(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
    }

    // ─── CREATE ─────────────────────────────────────────────────────────────────

    @Override
    public Student save(Student student) throws IOException {
        // Generate ID if not set
        if (student.getId() == null || student.getId().isBlank()) {
            student.setId("S" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }
        if (student.getCreatedAt() == null) {
            student.setCreatedAt(java.time.LocalDateTime.now().toString());
            student.setUpdatedAt(java.time.LocalDateTime.now().toString());
        }
        fileHandler.appendLine(fileHandler.getStudentsFile(), student.toFileString());
        return student;
    }

    // ─── READ ────────────────────────────────────────────────────────────────────

    @Override
    public Optional<Student> findById(String id) {
        return fileHandler.readAll(fileHandler.getStudentsFile())
                .stream()
                .filter(line -> line.startsWith(id + "|"))
                .map(Student::fromFileString)
                .findFirst();
    }

    @Override
    public List<Student> findAll() {
        return fileHandler.readAll(fileHandler.getStudentsFile())
                .stream()
                .map(Student::fromFileString)
                .collect(Collectors.toList());
    }

    /** Find by studentId field (e.g. "STU001") — not the internal UUID. */
    public Optional<Student> findByStudentId(String studentId) {
        return findAll().stream()
                .filter(s -> studentId.equals(s.getStudentId()))
                .findFirst();
    }

    /** Find by email — used for login. */
    public Optional<Student> findByEmail(String email) {
        return findAll().stream()
                .filter(s -> email.equalsIgnoreCase(s.getEmail()))
                .findFirst();
    }

    // ─── UPDATE ──────────────────────────────────────────────────────────────────

    @Override
    public Student update(Student student) throws IOException {
        student.updateTimestamp();
        boolean updated = fileHandler.updateLine(
                fileHandler.getStudentsFile(),
                student.getId(),
                student.toFileString()
        );
        if (!updated) throw new RuntimeException("Student not found: " + student.getId());
        return student;
    }

    // ─── DELETE ──────────────────────────────────────────────────────────────────

    @Override
    public boolean deleteById(String id) throws IOException {
        return fileHandler.deleteLine(fileHandler.getStudentsFile(), id);
    }

    // ─── UTILITY ─────────────────────────────────────────────────────────────────

    @Override
    public boolean existsById(String id) {
        return fileHandler.exists(fileHandler.getStudentsFile(), id);
    }

    @Override
    public long count() {
        return fileHandler.readAll(fileHandler.getStudentsFile()).size();
    }

    public boolean existsByEmail(String email) {
        return findAll().stream().anyMatch(s -> email.equalsIgnoreCase(s.getEmail()));
    }
}
