package com.example.courseregistrationsystem.repository;

import com.example.courseregistrationsystem.model.Grade;
import com.example.courseregistrationsystem.util.FileHandler;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * GradeRepository — CRUD for Grade via file handling.
 */
@Repository
public class GradeRepository implements FileRepository<Grade, String> {

    private final FileHandler fileHandler;

    public GradeRepository(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
    }

    @Override
    public Grade save(Grade grade) throws IOException {
        if (grade.getId() == null || grade.getId().isBlank()) {
            grade.setId("G" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
            grade.setGradeId(grade.getId());
        }
        grade.setCreatedAt(java.time.LocalDateTime.now().toString());
        grade.setUpdatedAt(java.time.LocalDateTime.now().toString());
        fileHandler.appendLine(fileHandler.getGradesFile(), grade.toFileString());
        return grade;
    }

    @Override
    public Optional<Grade> findById(String id) {
        return fileHandler.readAll(fileHandler.getGradesFile())
                .stream()
                .filter(line -> line.startsWith(id + "|"))
                .map(Grade::fromFileString)
                .findFirst();
    }

    @Override
    public List<Grade> findAll() {
        return fileHandler.readAll(fileHandler.getGradesFile())
                .stream()
                .map(Grade::fromFileString)
                .collect(Collectors.toList());
    }

    public List<Grade> findByStudentId(String studentId) {
        return findAll().stream()
                .filter(g -> studentId.equals(g.getStudentId()))
                .collect(Collectors.toList());
    }

    @Override
    public Grade update(Grade grade) throws IOException {
        grade.updateTimestamp();
        boolean updated = fileHandler.updateLine(
                fileHandler.getGradesFile(),
                grade.getId(),
                grade.toFileString()
        );
        if (!updated) throw new RuntimeException("Grade not found: " + grade.getId());
        return grade;
    }

    @Override
    public boolean deleteById(String id) throws IOException {
        return fileHandler.deleteLine(fileHandler.getGradesFile(), id);
    }

    @Override
    public boolean existsById(String id) {
        return fileHandler.exists(fileHandler.getGradesFile(), id);
    }

    @Override
    public long count() {
        return fileHandler.readAll(fileHandler.getGradesFile()).size();
    }
}
