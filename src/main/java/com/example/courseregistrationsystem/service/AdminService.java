package com.example.courseregistrationsystem.service;


import com.example.courseregistrationsystem.model.Course;
import com.example.courseregistrationsystem.model.Student;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminService {

    private final List<Student> students = new ArrayList<>();
    private final List<Course> courses = new ArrayList<>();

    public void addStudent(Student student) {
        students.add(student);
    }

    public void deleteStudent(int id) {
        students.removeIf(student -> student.getId() == "id");
    }

    public List<Student> getAllStudents() {
        return students;
    }

    public void addCourse(Course course) {
        courses.add(course);
    }

    public void deleteCourse(int id) {
        courses.removeIf(course -> course.getId() == "id");
    }

    public void updateCourse(int id, Course updatedCourse) {

        for (Course course : courses) {

            if (course.getId() == "id") {
                course.setCourseName(updatedCourse.getCourseName());
                course.setFee(updatedCourse.getFee());
            }
        }
    }

    public List<Course> getAllCourses() {
        return courses;
    }
}

