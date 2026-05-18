package com.example.courseregistrationsystem.model;

public class Course {

    private String id;
    private String courseName;
    private double fee;
    private String credits;
    private String lecturer;
    private String name;
    private String code;

    public Course(String datum, String s, String string, int i, String datum1, String s1) {
    }

    public Course(String id, String courseName, double fee , String credits,String lecturer) {
        this.id = id;
        this.courseName = courseName;
        this.fee = fee;
        this.credits=credits;
        this.lecturer=lecturer;
    }

    public static Course fromFileString(String line) {
        try {
            String[] data = line.split(",");

            return new Course(
                    data[0],
                    data[1],
                    Double.parseDouble(data[2]),
                    data[3],
                    data[4]
            );
        } catch (Exception e) {
            return null;
        }
    }

    public String toFileString() {
        return id + "," + courseName + "," + fee + "," + credits + "," + lecturer + "," + name + "," + code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public int getCredits() {
        try {
            return Integer.parseInt(credits);
        } catch (Exception e) {
            return 0;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCourseID() {
        return id;
    }
}