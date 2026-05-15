package com.example.courseregistrationsystem.repository;

import com.example.courseregistrationsystem.model.Course;
import com.example.courseregistrationsystem.util.FileHandler;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * CourseRepository — CRUD for Course via file handling.
 * Implements UML CourseManager: addCourse(), removeCourse(), getAllCourse().
 */
@Repository
public class CourseRepository implements FileRepository<Course, String> {

    private final FileHandler fileHandler;

    public CourseRepository(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
    }

    @Override
    public Course save(Course course) throws IOException {
        if (course.getId() == null || course.getId().isBlank()) {
            course.setId("C" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }
        if (course.getCourseId() == null || course.getCourseId().isBlank()) {
            course.setCourseId(course.getId());
        }
        course.setCreatedAt(java.time.LocalDateTime.now().toString());
        course.setUpdatedAt(java.time.LocalDateTime.now().toString());
        fileHandler.appendLine(fileHandler.getCoursesFile(), course.toFileString());
        return course;
    }

    @Override
    public Optional<Course> findById(String id) {
        return fileHandler.readAll(fileHandler.getCoursesFile())
                .stream()
                .filter(line -> line.startsWith(id + "|"))
                .map(Course::fromFileString)
                .findFirst();
    }

    @Override
    public List<Course> findAll() {
        return fileHandler.readAll(fileHandler.getCoursesFile())
                .stream()
                .map(Course::fromFileString)
                .collect(Collectors.toList());
    }

    public Optional<Course> findByCourseId(String courseId) {
        return findAll().stream()
                .filter(c -> courseId.equals(c.getCourseId()))
                .findFirst();
    }

    public Optional<Course> findByCode(String code) {
        return findAll().stream()
                .filter(c -> code.equalsIgnoreCase(c.getCode()))
                .findFirst();
    }

    @Override
    public Course update(Course course) throws IOException {
        course.updateTimestamp();
        boolean updated = fileHandler.updateLine(
                fileHandler.getCoursesFile(),
                course.getId(),
                course.toFileString()
        );
        if (!updated) throw new RuntimeException("Course not found: " + course.getId());
        return course;
    }

    @Override
    public boolean deleteById(String id) throws IOException {
        return fileHandler.deleteLine(fileHandler.getCoursesFile(), id);
    }

    @Override
    public boolean existsById(String id) {
        return fileHandler.exists(fileHandler.getCoursesFile(), id);
    }

    @Override
    public long count() {
        return fileHandler.readAll(fileHandler.getCoursesFile()).size();
    }
}
