package com.example.courseregistrationsystem.service;

import com.example.courseregistrationsystem.model.Course;
import com.example.courseregistrationsystem.model.Student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentService {

    @Autowired
    private FileStorageService fileStorageService;

    private final List<Student> students =
            new ArrayList<>();

    private final List<Course> registeredCourses =
            new ArrayList<>();

    // ================= REGISTER COURSE =================

    public void registerCourse(Course course) {

        registeredCourses.add(course);
    }

    public List<Course> getRegisteredCourses() {

        return registeredCourses;
    }

    // ================= SAVE STUDENT =================

    public void saveStudent(Student student) {

        String data =
                student.getId() + "," +
                        student.getName() + "," +
                        student.getEmail() + "," +
                        student.getDepartment();

        fileStorageService.writeToFile(
                "data/students.txt",
                data
        );
    }

    // ================= ADD STUDENT =================

    public Student registerStudent(
            Student student) {

        students.add(student);

        saveStudent(student);

        return student;
    }

    // ================= GET ALL STUDENTS =================

    public List<Student> getAllStudents() {

        return students;
    }

    // ================= UPDATE STUDENT =================

    public Student editStudent(
            String id,
            Student updatedStudent) {

        for (Student student : students) {

            if (student.getId().equals(id)) {

                student.setName(
                        updatedStudent.getName()
                );

                student.setEmail(
                        updatedStudent.getEmail()
                );

                student.setDepartment(
                        updatedStudent.getDepartment()
                );

                return student;
            }
        }

        return null;
    }

    // ================= DELETE STUDENT =================

    public boolean removeStudent(
            String id) {

        return students.removeIf(
                student ->
                        student.getId().equals(id)
        );
    }
}