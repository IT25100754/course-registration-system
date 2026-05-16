package com.example.courseregistrationsystem.service;

import com.example.courseregistrationsystem.model.Enrollment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GradeService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    public Enrollment updateGrade(Enrollment gradeRequest) {
        List<Enrollment> enrollments = enrollmentRepository.readEnrollments();
        boolean isUpdated = false;
        Enrollment updatedRecord = null;

        for (Enrollment enrollment : enrollments) {
            if (enrollment.getStudentId().equals(gradeRequest.getStudentId()) &&
                    enrollment.getCourseCode().equals(gradeRequest.getCourseCode())) {


                enrollment.setGrade(gradeRequest.getGrade());
                updatedRecord = enrollment;
                isUpdated = true;
                break; // Stop searching once found
            }
        }

        if (isUpdated) {
            enrollmentRepository.writeEnrollments(enrollments);
            return updatedRecord;
        }


        return null;
    }
}
