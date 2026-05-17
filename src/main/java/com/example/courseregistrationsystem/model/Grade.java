package com.example.courseregistrationsystem.model;

public class Grade  {

    private int id;
    private int studentId;
    private String subject;
    private String grade;

    public Grade() {
    }

    public Grade(int id, int studentId, String subject, String grade) {
        this.id = id;
        this.studentId = studentId;
        this.subject = subject;
        this.grade = grade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
