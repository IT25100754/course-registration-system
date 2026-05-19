package com.example.courseregistrationsystem.model;

import java.util.Collection;

public class Enrollment {

    private String id;
    private String studentId;
    private String courseId;
    private String enrolledDate;
    private String createdAt;
    private String grade;
    private int marks;
    private int year;
    private int semester;

    public Enrollment(String trim, String trimmed, int i) {
    }

    public Enrollment(String id, String studentId,
                      String courseId,
                      String enrolledDate,
                      String createdAt
   ) {

        this.id = id;
        this.studentId = studentId;
        this.courseId = courseId;
        this.enrolledDate = enrolledDate;
        this.createdAt = createdAt;
        this.grade=grade;
        this.year=year;
        this.semester=semester;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
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