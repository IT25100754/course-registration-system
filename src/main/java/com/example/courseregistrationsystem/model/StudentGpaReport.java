package com.example.courseregistrationsystem.model;

public class StudentGpaReport {
    private String studentId;
    private double gpa;

    public StudentGpaReport(String studentId, double gpa) {
        this.studentId = studentId;
        this.gpa = gpa;
    }


    public String getStudentId() {
        return studentId;
    }
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public double getGpa() {
        return gpa;
    }
    public void setGpa(double gpa) {
        this.gpa = gpa;
    }
}