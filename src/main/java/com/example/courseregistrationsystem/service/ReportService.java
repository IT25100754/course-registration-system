package com.example.courseregistrationsystem.service;

import com.example.courseregistrationsystem.model.Enrollment;
import com.example.courseregistrationsystem.model.Student;
import com.example.courseregistrationsystem.model.StudentGpaReport;
import com.example.courseregistrationsystem.repository.EnrollmentRepository;
import com.example.courseregistrationsystem.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private StudentRepository studentRepository;


    // 1. Get all students enrolled in a specific course
    public List<Student> getStudentsByCourse(String courseCode) {
        List<Enrollment> allEnrollments = enrollmentRepository.readEnrollments();
        List<Student> allStudents = studentRepository.readStudents();

        // Find all student IDs enrolled in the given course
        List<String> enrolledStudentIds = new ArrayList<>();
        for (Enrollment enrollment : allEnrollments) {
            if (courseCode.equals(enrollment.getCourseId())) {
                enrolledStudentIds.add(enrollment.getStudentId());
            }
        }

        // Filter the student list to only include those IDs
        return allStudents.stream()
                .filter(student -> enrolledStudentIds.contains(student.getId()))
                .collect(Collectors.toList());
    }

    // 2. Calculate GPA for all students
    public List<StudentGpaReport> calculateAllStudentGpas() {
        List<Enrollment> allEnrollments = enrollmentRepository.readEnrollments();
        Map<String, List<Enrollment>> studentEnrollmentMap = new HashMap<>();

        // Group enrollments by studentId (ignoring null grades)
        for (Enrollment enrollment : allEnrollments) {
            if (enrollment.getGrade() != null && !enrollment.getGrade().isEmpty()) {
                studentEnrollmentMap.computeIfAbsent(enrollment.getStudentId(), k -> new ArrayList<>()).add(enrollment);
            }
        }

        List<StudentGpaReport> gpaReports = new ArrayList<>();

        // Calculate GPA for each student
        for (Map.Entry<String, List<Enrollment>> entry : studentEnrollmentMap.entrySet()) {
            String studentId = entry.getKey();
            List<Enrollment> studentGrades = entry.getValue();

            double totalPoints = 0;
            for (Enrollment enrollment : studentGrades) {
                totalPoints += convertGradeToPoint((String) enrollment.getGrade());
            }

            // Calculate average and round to 1 decimal place (e.g., 3.8)
            double averageGpa = totalPoints / studentGrades.size();
            double roundedGpa = Math.round(averageGpa * 10.0) / 10.0;

            gpaReports.add(new StudentGpaReport(studentId, roundedGpa));
        }

        return gpaReports;
    }

    // Helper method to convert Letter Grade to GPA points
    private double convertGradeToPoint(String grade) {
        switch (grade.toUpperCase()) {
            case "A": return 4.0;
            case "B": return 3.0;
            case "C": return 2.0;
            case "D": return 1.0;
            case "F": return 0.0;
            default: return 0.0;
        }
    }
}