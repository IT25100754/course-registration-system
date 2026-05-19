package com.example.courseregistrationsystem.service;


import com.example.courseregistrationsystem.model.Course;
import com.example.courseregistrationsystem.model.Grade;
import com.example.courseregistrationsystem.model.Student;
import com.example.courseregistrationsystem.repository.CourseRepository;
import com.example.courseregistrationsystem.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminService {
    @Autowired
    private StudentRepository studentRepo;
    @Autowired
    private CourseRepository courseRepo;

    public void addStudent(Student student) {
        studentRepo.save(student);
    }
    public void deleteStudent(String id) {
        studentRepo.deleteById(id);
    }
    public List<Student> getAllStudents() {
        return studentRepo.findAll();
    }

    public void addCourse(Course course) {
        courseRepo.add(course);
    }
    public void deleteCourse(String id) {
        courseRepo.deleteById(id);
    }
    public List<Course> getAllCourses() {
        return courseRepo.findAll();
    }

    public void updateCourse(String id, Course updated) {
        updated.setId(id);
        courseRepo.update(updated);
    }

    public Object getAllGrades() {
        return null;
    }

    public void addGrade(Grade grade) {

    }

    public void deleteGrade(String id) {

    }
}

