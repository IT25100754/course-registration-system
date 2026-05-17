package com.example.courseregistrationsystem.service;

import com.example.courseregistrationsystem.model.Enrollment;
import com.example.courseregistrationsystem.model.Grade;
import com.example.courseregistrationsystem.repository.GradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GradeService {

    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private GradeRepository gradeRepository;

    public void addGrade(Grade grade) {
        gradeRepository.save(grade);
    }

    public List<Grade> getAllGrades() {
        return gradeRepository.findAll();
    }

    public void saveGrade(Grade grade) {

        String data = grade.getId() + "," + grade.getStudentId() + "," + grade.getSubject() + "," + grade.getGrade();

        fileStorageService.writeToFile("data/grades.txt", data);
    }

    public Enrollment updateGrade(Enrollment gradeRequest) {
        return gradeRequest;
    }
}