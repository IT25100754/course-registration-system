package com.example.courseregistrationsystem.model;

/**
 * OOP: INHERITANCE - both Enrollment and Grade extend BaseEntity.
 *      POLYMORPHISM - each gives its own toFileString() body.
 *      ENCAPSULATION - fields are private.
 */
public class Enrollment extends BaseEntity {

    private String studentId;
    private String courseId;
    private String enrolledDate;

    public Enrollment() { super(); }

    public Enrollment(String id, String studentId, String courseId,
                      String enrolledDate, String createdAt) {
        super(id, createdAt);
        this.studentId    = studentId;
        this.courseId     = courseId;
        this.enrolledDate = enrolledDate;
    }

    public String getStudentId()                       { return studentId; }
    public void   setStudentId(String s)               { this.studentId = s; }
    public String getCourseId()                        { return courseId; }
    public void   setCourseId(String c)                { this.courseId = c; }
    public String getEnrolledDate()                    { return enrolledDate; }
    public void   setEnrolledDate(String d)            { this.enrolledDate = d; }

    @Override
    public String toFileString() {
        return getId() + "|" + studentId + "|" + courseId + "|" + enrolledDate + "|" + getCreatedAt();
    }

    public static Enrollment fromFileString(String line) {
        String[] p = line.split("\\|", -1);
        if (p.length < 5) return null;
        return new Enrollment(p[0], p[1], p[2], p[3], p[4]);
    }
}

