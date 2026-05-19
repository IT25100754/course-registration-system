package com.example.courseregistrationsystem.service;

import com.example.courseregistrationsystem.model.Course;
import com.example.courseregistrationsystem.model.Student;
import com.example.courseregistrationsystem.dto.StudentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.courseregistrationsystem.repository.StudentRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private FileStorageService fileStorageService;

    private final List<Student> students =
            new ArrayList<>();

    private final List<Course> registeredCourses =
            new ArrayList<>();



    public void registerCourse(Course course) {
        registeredCourses.add(course);
    }

    public List<Course> getRegisteredCourses() {
        return registeredCourses;
    }



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



    public Student registerStudent(Student student) {
        students.add(student);
        saveStudent(student);
        return student;
    }



    public List<Student> getAllStudents() {
        return students;
    }



    public Student editStudent(String id, Student updatedStudent) {

        for (Student student : students) {

            if (student.getId().equals(id)) {

                student.setName(updatedStudent.getName());
                student.setEmail(updatedStudent.getEmail());
                student.setDepartment(updatedStudent.getDepartment());

                return student;
            }
        }

        return null;
    }



    public boolean removeStudent(String id) {

        return students.removeIf(
                student -> student.getId().equals(id)
        );
    }


    public StudentDTO.ApiResponse login(StudentDTO.LoginRequest req) {

        for (Student s : studentRepository.findAll()) {

            if (s.getEmail().equals(req.getEmail())
                    && s.getPassword().equals(req.getPassword())) {

                return new StudentDTO.ApiResponse(
                        true,
                        "Login successful",
                        s
                );
            }
        }

        return new StudentDTO.ApiResponse(
                false,
                "Invalid email or password"
        );
    }


    public StudentDTO.ApiResponse register(StudentDTO.RegisterRequest req) {

        for (Student s : studentRepository.findAll()) {

            if (s.getEmail().equals(req.getEmail())) {

                return new StudentDTO.ApiResponse(
                        false,
                        "Email already exists"
                );
            }
        }

        Student student = new Student();

        student.setName(req.getName());
        student.setEmail(req.getEmail());
        student.setPassword(req.getPassword());
        student.setStudentId(req.getStudentId());
        student.setPhone(req.getPhone());
        student.setDepartment("IT");

        studentRepository.save(student);

        return new StudentDTO.ApiResponse(
                true,
                "Student registered successfully"
        );
    }


    public StudentDTO.ApiResponse resetPassword(StudentDTO.ResetPasswordRequest req) {

        for (Student s : students) {

            if (s.getEmail().equals(req.getEmail())) {

                if (!req.getNewPassword().equals(req.getConfirmPassword())) {
                    return new StudentDTO.ApiResponse(false, "Passwords do not match");
                }

                s.setPassword(req.getNewPassword());
                return new StudentDTO.ApiResponse(true, "Password reset successful");
            }
        }

        return new StudentDTO.ApiResponse(false, "Email not found");
    }


    public Student getProfile(String studentId) {

        for (Student s : students) {
            if (s.getStudentId().equals(studentId)) {
                return s;
            }
        }

        return null;
    }


    public boolean updatePhone(String studentId, String phone) {

        for (Student s : students) {
            if (s.getStudentId().equals(studentId)) {
                s.setPhone(phone);
                return true;
            }
        }

        return false;
    }


    public boolean changePassword(String studentId, String newPassword) {

        for (Student s : students) {
            if (s.getStudentId().equals(studentId)) {
                s.setPassword(newPassword);
                return true;
            }
        }

        return false;
    }
}