package com.example.courseregistrationsystem.service;

import com.example.courseregistrationsystem.dto.StudentDTO;
import com.example.courseregistrationsystem.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private static final String COURSES_FILE = "courses.txt";
    private static final String ENROLLMENTS_FILE = "enrollments.txt";
    private static final String GRADES_FILE = "grades.txt";

    private final FileStorageService fileStorage;

    @Autowired
    public DashboardService(FileStorageService fileStorage) {
        this.fileStorage = fileStorage;
        seedSampleData();
    }

    // ================= LOAD METHODS =================

    private List<Course> loadCourses() {

        return fileStorage.readLines(COURSES_FILE)
                .stream()
                .map(Course::fromFileString)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private List<Enrollment> loadEnrollments() {

        return fileStorage.readLines(ENROLLMENTS_FILE)
                .stream()
                .map(Enrollment::fromFileString)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private List<Grade> loadGrades() {

        return fileStorage.readLines(GRADES_FILE)
                .stream()
                .map(line ->
                        Grade.fromFileString(line))
                .filter(Objects::nonNull)
                .toList();
    }

    // ================= SAVE METHODS =================

    private void saveEnrollments(List<Enrollment> enrollments) {

        List<String> lines = enrollments.stream()
                .map(Enrollment::toFileString)
                .collect(Collectors.toList());

        fileStorage.writeLines(ENROLLMENTS_FILE, lines);
    }

    // ================= UTILITY METHODS =================

    private String now() {

        return LocalDateTime.now()
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    private double gradePoints(int marks) {

        if (marks >= 90) return 4.0;
        if (marks >= 80) return 3.5;
        if (marks >= 70) return 3.0;
        if (marks >= 60) return 2.5;

        return 2.0;
    }

    // ================= SAMPLE DATA =================

    private void seedSampleData() {

        if (loadCourses().isEmpty()) {

            String ts = now();

            List<String> courseLines = Arrays.asList(

                    new Course(
                            "C101",
                            "Data Structures",
                            "CS201",
                            4,
                            "Dr. Smith",
                            ts
                    ).toFileString(),

                    new Course(
                            "C102",
                            "Database Systems",
                            "CS301",
                            3,
                            "Dr. Johnson",
                            ts
                    ).toFileString(),

                    new Course(
                            "C103",
                            "Web Development",
                            "CS401",
                            3,
                            "Prof. Brown",
                            ts
                    ).toFileString()
            );

            fileStorage.writeLines(COURSES_FILE, courseLines);
        }

        if (loadEnrollments().isEmpty()) {

            fileStorage.writeLines(
                    ENROLLMENTS_FILE,
                    new ArrayList<>()
            );
        }

        if (loadGrades().isEmpty()) {

            fileStorage.writeLines(
                    GRADES_FILE,
                    new ArrayList<>()
            );
        }
    }

    // ================= DASHBOARD =================

    public StudentDTO.DashboardResponse getDashboardData(
            String studentId) {

        List<Course> allCourses = loadCourses();
        List<Enrollment> enrollments = loadEnrollments();
        List<Grade> grades = loadGrades();

        List<Enrollment> mine = enrollments.stream()
                .filter(e -> e.getStudentId()
                        .equalsIgnoreCase(studentId))
                .collect(Collectors.toList());

        List<Grade> myGrades = grades.stream()
                .filter(g -> Boolean.parseBoolean(g.getStudentId()))
                .collect(Collectors.toList());

        int totalCredits = 0;
        double totalPoints = 0.0;
        double totalMarks = 0.0;

        for (Enrollment e : mine) {

            Course c = allCourses.stream()
                    .filter(x -> x.getId()
                            .equals(e.getCourseId()))
                    .findFirst()
                    .orElse(null);

            if (c != null) {
                totalCredits += c.getCredits();
            }
        }

        for (Grade g : myGrades) {

            Course c = allCourses.stream()
                    .filter(x -> x.getId()
                            .equals(g.getCourseId()))
                    .findFirst()
                    .orElse(null);

            int credits = c != null
                    ? c.getCredits()
                    : 3;

            totalPoints +=
                    gradePoints(g.getMarks())
                            * credits;

            totalMarks += g.getMarks();
        }

        double gpa = totalCredits > 0
                ? totalPoints / totalCredits
                : 0.0;

        double avgScore = myGrades.isEmpty()
                ? 0.0
                : totalMarks / myGrades.size();

        List<StudentDTO.DashboardResponse.GradeInfo>
                gradeInfos = myGrades.stream()
                .map(g -> {

                    Course c = allCourses.stream()
                            .filter(x -> x.getId()
                                    .equals(g.getCourseId()))
                            .findFirst()
                            .orElse(null);

                    StudentDTO.DashboardResponse
                            .GradeInfo gi =
                            new StudentDTO
                                    .DashboardResponse
                                    .GradeInfo();

                    gi.setCourseName(
                            c != null
                                    ? c.getName()
                                    : "Unknown"
                    );

                    gi.setCourseCode(
                            c != null
                                    ? c.getCode()
                                    : "N/A"
                    );

                    gi.setMarks(g.getMarks());
                    gi.setGrade(g.getGrade());

                    gi.setStatus(
                            g.getMarks() >= 60
                                    ? "Passed"
                                    : "Failed"
                    );

                    return gi;
                })
                .collect(Collectors.toList());

        StudentDTO.DashboardResponse res =
                new StudentDTO.DashboardResponse();

        res.setStudentId(studentId);
        res.setFaculty(
                "Harvard Faculty of Computing"
        );

        res.setEnrolledCourseCount(
                mine.size()
        );

        res.setGpa(
                Math.round(gpa * 100.0) / 100.0
        );

        res.setTotalCredits(totalCredits);

        res.setAverageScore(
                Math.round(avgScore * 10.0) / 10.0
        );

        res.setGrades(gradeInfos);

        return res;
    }

    // ================= COURSE METHODS =================

    public List<Course> getAvailableCourses(
            String studentId) {

        Set<String> enrolled = loadEnrollments()
                .stream()
                .filter(e -> e.getStudentId()
                        .equalsIgnoreCase(studentId))
                .map(Enrollment::getCourseId)
                .collect(Collectors.toSet());

        return loadCourses().stream()
                .filter(c ->
                        !enrolled.contains(c.getId()))
                .collect(Collectors.toList());
    }

    public List<Course> getEnrolledCourses(
            String studentId) {

        Set<String> enrolled = loadEnrollments()
                .stream()
                .filter(e -> e.getStudentId()
                        .equalsIgnoreCase(studentId))
                .map(Enrollment::getCourseId)
                .collect(Collectors.toSet());

        return loadCourses().stream()
                .filter(c ->
                        enrolled.contains(c.getId()))
                .collect(Collectors.toList());
    }

    // ================= REGISTER =================

    public StudentDTO.ApiResponse registerCourse(
            String studentId,
            String courseId) {

        List<Enrollment> all =
                loadEnrollments();

        boolean alreadyEnrolled =
                all.stream().anyMatch(
                        e -> e.getStudentId()
                                .equalsIgnoreCase(studentId)
                                &&
                                e.getCourseId()
                                        .equals(courseId)
                );

        if (alreadyEnrolled) {

            return new StudentDTO.ApiResponse(
                    false,
                    "Already enrolled"
            );
        }

        Enrollment newEnrollment =
                new Enrollment(
                        UUID.randomUUID().toString(),
                        studentId,
                        courseId,
                        LocalDate.now().toString(),
                        now()
                );

        all.add(newEnrollment);

        saveEnrollments(all);

        return new StudentDTO.ApiResponse(
                true,
                "Registered Successfully"
        );
    }

    // ================= UNREGISTER =================

    public StudentDTO.ApiResponse unregisterCourse(
            String studentId,
            String courseId) {

        List<Enrollment> all =
                loadEnrollments();

        List<Enrollment> updated =
                all.stream()
                        .filter(e ->
                                !(e.getStudentId()
                                        .equalsIgnoreCase(studentId)
                                        &&
                                        e.getCourseId()
                                                .equals(courseId)))
                        .collect(Collectors.toList());

        if (updated.size() == all.size()) {

            return new StudentDTO.ApiResponse(
                    false,
                    "Enrollment not found"
            );
        }

        saveEnrollments(updated);

        return new StudentDTO.ApiResponse(
                true,
                "Unregistered Successfully"
        );
    }

    // ================= GRADES =================

//    public List<Grade> getGrades(
//            String studentId) {
//
//        return loadGrades().stream()
//                .filter(g -> g.getStudentId())
//                .collect(Collectors.toList());
//    }

    // ================= ALL COURSES =================

    public List<Course> getAllCourses() {

        return loadCourses();
    }


    public List<Grade> getClass(String studentId) {
        return null;
    }
}