package com.example.courseregistrationsystem.model;

public class Course extends BaseEntity {

    private int credits;



    @Override
    public String toFileString() {

        return "";
    }

    public static Course fromFileString(String line) {

        return null;
    }

    public String getCourseName() {
        return "";
    }

    public void setCourseID(String courseID) {
    }
}
