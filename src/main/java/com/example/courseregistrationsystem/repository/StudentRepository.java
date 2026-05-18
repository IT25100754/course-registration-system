package com.example.courseregistrationsystem.repository;

import com.example.courseregistrationsystem.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class StudentRepository {

    @Autowired private FileHandler fileHandler;

    public List<Student> findAll() {
        return fileHandler.readAllLines(fileHandler.getStudentsFile()).stream()
                .map(Student::fromFileString).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public void save(Student student) {
        List<Student> all = findAll();
        all.removeIf(s -> s.getId().equals(student.getId()));
        all.add(student);
        fileHandler.writeAllLines(fileHandler.getStudentsFile(),
                all.stream().map(Student::toFileString).collect(Collectors.toList()));
    }

    public Optional<Student> findByEmail(String email) {
        return findAll().stream().filter(s -> s.getEmail().equalsIgnoreCase(email)).findFirst();
    }

    public void deleteById(String id) {
        List<String> lines = findAll().stream()
                .filter(s -> !s.getId().equals(id))
                .map(Student::toFileString).collect(Collectors.toList());
        fileHandler.writeAllLines(fileHandler.getStudentsFile(), lines);
    }
}