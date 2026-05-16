package com.example.courseregistrationsystem.service;

import com.example.courseregistrationsystem.model.Course;
import com.example.courseregistrationsystem.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    // ---- CREATE ----

    public String addCourse(Course course) {
        if (course.getCourseName() == null || course.getCourseName().trim().isEmpty()) {
            return "ERROR: Course name cannot be empty.";
        }
        if (course.getCredits() < 1 || course.getCredits() > 6) {
            return "ERROR: Credits must be between 1 and 6.";
        }
        if (course.getAvailableSeats() < 0) {
            return "ERROR: Available seats cannot be negative.";
        }
        // Auto-generate course ID
        if (course.getCourseID() == null || course.getCourseID().trim().isEmpty()) {
            course.setCourseID(courseRepository.generateNextId());
        } else if (courseRepository.existsById(course.getCourseID())) {
            return "ERROR: Course ID '" + course.getCourseID() + "' already exists.";
        }
        courseRepository.save(course);
        return "SUCCESS: Course '" + course.getCourseName() + "' added with ID: " + course.getCourseID();
    }

    // ---- READ ----

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Optional<Course> getCourseById(String courseID) {
        return courseRepository.findById(courseID);
    }

    // ---- UPDATE ----

    public String updateCourse(Course course) {
        if (!courseRepository.existsById(course.getCourseID())) {
            return "ERROR: Course ID '" + course.getCourseID() + "' not found.";
        }
        courseRepository.update(course);
        return "SUCCESS: Course updated successfully.";
    }

    // ---- DELETE ----

    public String deleteCourse(String courseID) {
        boolean deleted = courseRepository.deleteById(courseID);
        return deleted ? "SUCCESS: Course '" + courseID + "' deleted."
                : "ERROR: Course not found.";
    }

    // ---- SEAT MANAGEMENT (called by RegistrationService) ----

    public boolean decreaseSeat(String courseID) {
        Optional<Course> opt = courseRepository.findById(courseID);
        if (opt.isPresent() && opt.get().hasAvailableSeats()) {
            Course c = opt.get();
            c.decreaseSeat();
            courseRepository.update(c);
            return true;
        }
        return false;
    }

    public void increaseSeat(String courseID) {
        courseRepository.findById(courseID).ifPresent(c -> {
            c.increaseSeat();
            courseRepository.update(c);
        });
    }

    public boolean existsById(String courseID) {
        return courseRepository.existsById(courseID);
    }
}