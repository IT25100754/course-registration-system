package com.example.courseregistrationsystem.repository;

import com.example.courseregistrationsystem.model.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Repository
public class CourseRepository {

    @Autowired
    private FileHandler fileHandler;

    // ---- CREATE ----

    public void save(Course course) {
        fileHandler.appendLine(fileHandler.getCoursesFile(), course.toFileString());
    }

    // ---- READ ----

    public List<Course> findAll() {
        List<String> lines   = fileHandler.readAllLines(fileHandler.getCoursesFile());
        List<Course> courses = new ArrayList<>();
        for (String line : lines) {
            Course c = Course.fromFileString(line);
            if (c != null) courses.add(c);
        }
        return courses;
    }

    public Optional<Course> findById(String courseID) {
        return findAll().stream()
                .filter(c -> c.getCourseID().equalsIgnoreCase(courseID))
                .findFirst();
    }

    // ---- UPDATE ----


    public boolean update(Course updatedCourse) {
        List<Course>  all   = findAll();
        boolean       found = false;
        List<String>  lines = new ArrayList<>();

        for (Course c : all) {
            if (c.getCourseID().equals(updatedCourse.getCourseID())) {
                lines.add(updatedCourse.toFileString());
                found = true;
            } else {
                lines.add(c.toFileString());
            }
        }
        if (found) fileHandler.writeAllLines(fileHandler.getCoursesFile(), lines);
        return found;
    }

    // ---- DELETE ----

    public boolean deleteById(String courseID) {
        List<Course>  all      = findAll();
        List<String>  remaining = new ArrayList<>();

        for (Course c : all) {
            if (!c.getCourseID().equals(courseID)) {
                remaining.add(c.toFileString());
            }
        }
        fileHandler.writeAllLines(fileHandler.getCoursesFile(), remaining);
        return remaining.size() < all.size();
    }

    public boolean existsById(String courseID) {
        return findById(courseID).isPresent();
    }


    public String generateNextId() {
        List<Course> all = findAll();
        int maxNum = 100;
        for (Course c : all) {
            try {
                int num = Integer.parseInt(c.getCourseID().replaceAll("[^0-9]", ""));
                if (num > maxNum) maxNum = num;
            } catch (NumberFormatException ignored) {}
        }
        return "CS" + (maxNum + 1);
    }
}