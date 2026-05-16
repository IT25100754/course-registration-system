package com.example.courseregistrationsystem.model;

/**
 * OOP: INHERITANCE - extends BaseEntity.
 *      POLYMORPHISM - overrides toFileString().
 */
public class Grade extends BaseEntity {

    private String studentId;
    private String courseId;
    private int    marks;
    private String grade;

    public Grade() { super(); }

    public Grade(String id, String studentId, String courseId,
                 int marks, String grade, String createdAt) {
        super(id, createdAt);
        this.studentId = studentId;
        this.courseId  = courseId;
        this.marks     = marks;
        this.grade     = grade;
    }

    public String getStudentId()               { return studentId; }
    public void   setStudentId(String s)       { this.studentId = s; }
    public String getCourseId()                { return courseId; }
    public void   setCourseId(String c)        { this.courseId = c; }
    public int    getMarks()                   { return marks; }
    public void   setMarks(int m)              { this.marks = m; }
    public String getGrade()                   { return grade; }
    public void   setGrade(String g)           { this.grade = g; }

    @Override
    public String toFileString() {
        return getId() + "|" + studentId + "|" + courseId + "|" + marks + "|" + grade + "|" + getCreatedAt();
    }

    public static Grade fromFileString(String line) {
        String[] p = line.split("\\|", -1);
        if (p.length < 6) return null;
        return new Grade(p[0], p[1], p[2], Integer.parseInt(p[3]), p[4], p[5]);
    }
}

