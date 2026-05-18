package com.example.courseregistrationsystem.model;

public class Enrollment extends BaseEntity {

    private String studentId;
    private String courseId;
    private String enrolledDate;

    public Enrollment() {
        super();
    }
    public Enrollment(String id, String studentId, int courseId) {
        this.studentId = studentId;
        this.courseId = String.valueOf(courseId);
    }

    public Enrollment(String id,
                      String studentId,
                      String courseId,
                      String enrolledDate,
                      String createdAt) {

        super.setId(id);
        super.setCreatedAt(createdAt);

        this.studentId = studentId;
        this.courseId = courseId;
        this.enrolledDate = enrolledDate;
    }

    public static Enrollment fromFileString(String line) {

        try {
            String[] data = line.split("\\|", -1);

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
        return getId() + "|" +
                studentId + "|" +
                courseId + "|" +
                enrolledDate + "|" +
                getCreatedAt();
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getEnrolledDate() {
        return enrolledDate;
    }

    public void setEnrolledDate(String enrolledDate) {
        this.enrolledDate = enrolledDate;
    }
}