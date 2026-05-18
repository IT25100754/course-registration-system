package com.example.courseregistrationsystem.repository;

import com.example.courseregistrationsystem.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Repository
public class StudentRepository {

    @Autowired
    private com.example.courseregistrationsystem.repository.FileHandler fileHandler;

    // ================================================================
    //  CREATE - Save a new student to students.txt
    // ================================================================


    public void save(Student student) {
        fileHandler.appendLine(
                fileHandler.getStudentsFile(),
                student.toFileString()
        );
    }

    // ================================================================
    //  READ - Load all students from students.txt
    // ================================================================


    public List<Student> findAll() {
        List<String> lines    = fileHandler.readAllLines(fileHandler.getStudentsFile());
        List<Student> students = new ArrayList<>();

        for (String line : lines) {
            Student s = Student.fromFileString(line);
            if (s != null) {
                students.add(s);
            }
        }
        return students;
    }


    public Optional<Student> findById(String studentID) {
        return findAll().stream()
                .filter(s -> s.getStudentID().equalsIgnoreCase(studentID))
                .findFirst();
    }

    public Optional<Student> findByEmail(String email) {
        return findAll().stream()
                .filter(s -> s.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    // ================================================================
    //  UPDATE - Modify an existing student record in students.txt
    // ================================================================


    public boolean update(Student updatedStudent) {
        List<Student> all = findAll();
        boolean found = false;

        List<String> updatedLines = new ArrayList<>();
        for (Student s : all) {
            if (s.getStudentID().equals(updatedStudent.getStudentID())) {
                updatedLines.add(updatedStudent.toFileString()); // replace with updated
                found = true;
            } else {
                updatedLines.add(s.toFileString()); // keep existing
            }
        }

        if (found) {
            fileHandler.writeAllLines(fileHandler.getStudentsFile(), updatedLines);
        }
        return found;
    }

    // ================================================================
    //  DELETE - Remove a student from students.txt
    // ================================================================


    public boolean deleteById(String studentID) {
        List<Student> all = findAll();
        int originalSize = all.size();

        List<String> remaining = new ArrayList<>();
        for (Student s : all) {
            if (!s.getStudentID().equals(studentID)) {
                remaining.add(s.toFileString());
            }
        }

        fileHandler.writeAllLines(fileHandler.getStudentsFile(), remaining);
        return remaining.size() < originalSize; // true if one was removed
    }


    public boolean existsById(String studentID) {
        return findById(studentID).isPresent();
    }


    public String generateNextId() {
        List<Student> all = findAll();
        int maxNum = 0;
        for (Student s : all) {
            try {
                int num = Integer.parseInt(s.getStudentID().replaceAll("[^0-9]", ""));
                if (num > maxNum) maxNum = num;
            } catch (NumberFormatException ignored) {}
        }
        return String.format("STU%03d", maxNum + 1);
    }

    public List<Student> readStudents() {
        return null;
    }
}