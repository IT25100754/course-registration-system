package com.example.courseregistrationsystem.model;

public class Grade {

    private String id;
    private String studentId;
    private String courseId;
    private int marks;
    private String grade;
    private String subject;
    private int year;
    private int semester;


    public Grade(String datum, String s, String string, int i, String datum1) {
    }

    public Grade(String id, String studentId, String courseId, int marks, String grade,int year,int semester) {
        this.id = id;
        this.studentId = studentId;
        this.courseId = courseId;
        this.marks = marks;
        this.grade = grade;
        this.year=year;
        this.semester=semester;


    }

    // IMPORTANT METHOD
    public static Grade fromFileString(
            String line) {

        try {

            String[] data = line.split(",");

            return new Grade(data[0], data[1], data[2], Integer.parseInt(data[3]), data[4]);

        } catch (Exception e) {
            return null;
        }
    }

    public String toFileString() {

        return id + "," + studentId + "," + courseId + "," + marks + "," + grade;
    }

    public String getId() {
        return id;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getCourseId() {
        return courseId;
    }

    public int getMarks() {
        return marks;
    }

    public String getGrade() {
        return grade;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }
}