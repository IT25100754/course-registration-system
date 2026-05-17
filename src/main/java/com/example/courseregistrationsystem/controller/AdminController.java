package com.example.courseregistrationsystem.controller;


import com.example.courseregistrationsystem.model.Course;
import com.example.courseregistrationsystem.model.Student;
import com.example.courseregistrationsystem.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/students")
    public String addStudent(@RequestBody Student student) {

        adminService.addStudent(student);
        return "Student Added Successfully";
    }

    @DeleteMapping("/students/{id}")
    public String deleteStudent(@PathVariable int id) {

        adminService.deleteStudent(id);
        return "Student Deleted Successfully";
    }

    @GetMapping("/students")
    public List<Student> getAllStudents() {
        return adminService.getAllStudents();
    }

    @PostMapping("/courses")
    public String addCourse(@RequestBody Course course) {

        adminService.addCourse(course);
        return "Course Added Successfully";
    }

    @PutMapping("/courses/{id}")
    public String updateCourse(@PathVariable int id,
                               @RequestBody Course course) {

        adminService.updateCourse(id, course);
        return "Course Updated Successfully";
    }

    @DeleteMapping("/courses/{id}")
    public String deleteCourse(@PathVariable int id) {

        adminService.deleteCourse(id);
        return "Course Deleted Successfully";
    }

    @GetMapping("/courses")
    public List<Course> getAllCourses() {
        return adminService.getAllCourses();
    }
}