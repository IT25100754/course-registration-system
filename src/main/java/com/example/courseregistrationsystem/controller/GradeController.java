package com.example.courseregistrationsystem.controller;

import com.example.courseregistrationsystem.model.Enrollment;
import com.example.courseregistrationsystem.service.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/enrollments")
public class GradeController {

    @Autowired
    private GradeService gradeService;

    // PUT: Updates the grade for a specific student and course
    @PutMapping("/grade")
    public ResponseEntity<Enrollment> inputGrade(@RequestBody Enrollment gradeRequest) {

        Enrollment updatedEnrollment = gradeService.updateGrade(gradeRequest);

        if (updatedEnrollment != null) {
            return ResponseEntity.ok(updatedEnrollment);
        } else {
            // Returns a 404 Not Found if the student isn't enrolled in that course
            return ResponseEntity.notFound().build();
        }
    }
}







