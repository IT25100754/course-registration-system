package com.example.courseregistrationsystem.repository;

import com.example.courseregistrationsystem.model.Grade;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class GradeRepository {

    private final List<Grade> grades = new ArrayList<>();

    public void save(Grade grade) {
        grades.add(grade);
    }

    public List<Grade> findAll() {
        return grades;
    }
}

