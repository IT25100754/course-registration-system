package com.example.courseregistrationsystem.model;

import java.util.Collection;
import java.util.Collections;

public class Enrollment {

    private String id;
    private String studentId;
    private String courseId;
    private String enrolledDate;
    private String createdAt;
    private String grade;
    private int marks;
    private String semester;
    private String year;


    public Enrollment(String trim, String trimmed, int i) {
    }

    public Enrollment(String id,
                      String studentId,
                      String courseId,
                      String enrolledDate,
                      String createdAt) {

        this.id = id;
        this.studentId = studentId;
        this.courseId = courseId;
        this.enrolledDate = enrolledDate;
        this.createdAt = createdAt;
    }

    public static Enrollment fromFileString(
            String line) {

        try {

            String[] data = line.split(",");

            return new Enrollment(
                    data[0],
                    data[1],
                    data[2],
                    data[3],
                    data[4]
            );

        } catch (Exception e) {

            return null;
        }
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getGrade() {
        return grade;

    }

    public String toFileString() {

        return id + "," +
                studentId + "," +
                courseId + "," +
                enrolledDate + "," +
                createdAt;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getCourseId() {
        return courseId;
    }
}


