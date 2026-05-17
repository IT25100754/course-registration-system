package com.example.courseregistrationsystem.controller;

import com.example.courseregistrationsystem.model.Student;
import com.example.courseregistrationsystem.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    // GET: Retrieves all students from students.json
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(Collections.singletonList((Student) studentService.getAllStudents()));
    }

    // POST: Registers a new student to the system
    @PostMapping
    public ResponseEntity<Student> registerStudent(@RequestBody Student student) {
        Student createdStudent = studentService.registerStudent(student);
        return ResponseEntity.ok(createdStudent);
    }

    // PUT: Edits student details
    @PutMapping("/{id}")
    public ResponseEntity<Student> editStudent(@PathVariable String id, @RequestBody Student student) {
        Student updatedStudent = studentService.editStudent(id, student);
        if (updatedStudent != null) {
            return ResponseEntity.ok(updatedStudent);
        }
        return ResponseEntity.notFound().build();
    }

    // DELETE: Removes a student
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeStudent(@PathVariable String id) {
        boolean removed = studentService.removeStudent(id);
        if (removed) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}
