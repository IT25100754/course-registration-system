package com.example.courseregistrationsystem.model;

public class Grade {
    private int id;
    private String studentName;
    private String gradeID;


    public Grade(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getGradeID() {
        return gradeID;
    }
}
