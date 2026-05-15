package com.example.courseregistrationsystem.repository;

import com.example.courseregistrationsystem.model.Registration;
import com.example.courseregistrationsystem.util.FileHandler;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * RegistrationRepository — CRUD for Registration via file handling.
 * Implements UML RegistrationManager: registerStudent(), dropStudents(), getStudentCourse().
 */
@Repository
public class RegistrationRepository implements FileRepository<Registration, String> {

    private final FileHandler fileHandler;

    public RegistrationRepository(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
    }

    @Override
    public Registration save(Registration registration) throws IOException {
        if (registration.getId() == null || registration.getId().isBlank()) {
            registration.setId("R" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }
        if (registration.getRegistrationId() == null || registration.getRegistrationId().isBlank()) {
            registration.setRegistrationId(registration.getId());
        }
        registration.setCreatedAt(java.time.LocalDateTime.now().toString());
        registration.setUpdatedAt(java.time.LocalDateTime.now().toString());
        fileHandler.appendLine(fileHandler.getRegistrationsFile(), registration.toFileString());
        return registration;
    }

    @Override
    public Optional<Registration> findById(String id) {
        return fileHandler.readAll(fileHandler.getRegistrationsFile())
                .stream()
                .filter(line -> line.startsWith(id + "|"))
                .map(Registration::fromFileString)
                .findFirst();
    }

    @Override
    public List<Registration> findAll() {
        return fileHandler.readAll(fileHandler.getRegistrationsFile())
                .stream()
                .map(Registration::fromFileString)
                .collect(Collectors.toList());
    }

    /** Find all registrations for a student — getStudentCourse() from UML. */
    public List<Registration> findByStudentId(String studentId) {
        return findAll().stream()
                .filter(r -> studentId.equals(r.getStudentId()))
                .collect(Collectors.toList());
    }

    /** Find all registrations for a course — for roster. */
    public List<Registration> findByCourseId(String courseId) {
        return findAll().stream()
                .filter(r -> courseId.equals(r.getCourseId()))
                .collect(Collectors.toList());
    }

    /** Check if a student is already registered in a course. */
    public boolean existsByStudentAndCourse(String studentId, String courseId) {
        return findAll().stream()
                .anyMatch(r -> studentId.equals(r.getStudentId())
                        && courseId.equals(r.getCourseId())
                        && !"DROPPED".equals(r.getStatus()));
    }

    @Override
    public Registration update(Registration registration) throws IOException {
        registration.updateTimestamp();
        boolean updated = fileHandler.updateLine(
                fileHandler.getRegistrationsFile(),
                registration.getId(),
                registration.toFileString()
        );
        if (!updated) throw new RuntimeException("Registration not found: " + registration.getId());
        return registration;
    }

    @Override
    public boolean deleteById(String id) throws IOException {
        return fileHandler.deleteLine(fileHandler.getRegistrationsFile(), id);
    }

    @Override
    public boolean existsById(String id) {
        return fileHandler.exists(fileHandler.getRegistrationsFile(), id);
    }

    @Override
    public long count() {
        return fileHandler.readAll(fileHandler.getRegistrationsFile()).size();
    }

    public long countByCourseId(String courseId) {
        return findByCourseId(courseId).stream()
                .filter(r -> !"DROPPED".equals(r.getStatus()))
                .count();
    }
}
