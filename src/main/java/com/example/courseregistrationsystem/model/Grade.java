package com.example.courseregistrationsystem.model;

/**
 * Grade entity.
 *
 * OOP Concepts:
 * - INHERITANCE: Extends BaseEntity
 * - ENCAPSULATION: Private fields
 * - POLYMORPHISM: calculateGrade() is a domain-specific polymorphic behaviour
 *
 * File format:
 *   id|gradeId|studentId|courseId|marks|grade|createdAt|updatedAt
 */
public class Grade extends BaseEntity {

    private String gradeId;
    private String studentId;
    private String courseId;
    private double marks;
    private String grade; // A, B+, B, C+, C, D, F

    // ─── Constructors ───────────────────────────────────────────────────────────

    public Grade() { super(); }

    public Grade(String id, String gradeId, String studentId,
                 String courseId, double marks) {
        super(id);
        this.gradeId   = gradeId;
        this.studentId = studentId;
        this.courseId  = courseId;
        this.marks     = marks;
        this.grade     = calculateGrade(marks);
    }

    // ─── Polymorphism — grade calculation ───────────────────────────────────────

    @Override
    public String toFileString() {
        return String.join("|",
                getId(),
                gradeId,
                studentId,
                courseId,
                String.valueOf(marks),
                grade,
                getCreatedAt(),
                getUpdatedAt()
        );
    }

    @Override
    public String getDisplayName() {
        return "Grade[" + gradeId + "] " + studentId + " → " + courseId + ": " + marks + " (" + grade + ")";
    }

    /**
     * Polymorphic grade calculation.
     * Can be overridden by subclasses (e.g. GraduateGrade with different scale).
     */
    public String calculateGrade(double marks) {
        if (marks >= 90) return "A";
        if (marks >= 80) return "B+";
        if (marks >= 70) return "B";
        if (marks >= 60) return "C+";
        if (marks >= 50) return "C";
        if (marks >= 40) return "D";
        return "F";
    }

    public double calculateGPA() {
        if (marks >= 90) return 4.0;
        if (marks >= 80) return 3.5;
        if (marks >= 70) return 3.0;
        if (marks >= 60) return 2.5;
        if (marks >= 50) return 2.0;
        if (marks >= 40) return 1.5;
        return 0.0;
    }

    public static Grade fromFileString(String line) {
        String[] parts = line.split("\\|", -1);
        if (parts.length < 6) throw new IllegalArgumentException("Invalid grade record: " + line);

        Grade g = new Grade();
        g.setId(parts[0]);
        g.setGradeId(parts[1]);
        g.setStudentId(parts[2]);
        g.setCourseId(parts[3]);
        g.setMarks(Double.parseDouble(parts[4]));
        g.setGrade(parts[5]);
        if (parts.length > 6) g.setCreatedAt(parts[6]);
        if (parts.length > 7) g.setUpdatedAt(parts[7]);
        return g;
    }

    // ─── Getters & Setters ───────────────────────────────────────────────────────

    public String getGradeId()                   { return gradeId; }
    public void   setGradeId(String gradeId)     { this.gradeId = gradeId; }

    public String getStudentId()                 { return studentId; }
    public void   setStudentId(String studentId) { this.studentId = studentId; }

    public String getCourseId()                  { return courseId; }
    public void   setCourseId(String courseId)   { this.courseId = courseId; }

    public double getMarks()                     { return marks; }
    public void   setMarks(double marks)         { this.marks = marks; this.grade = calculateGrade(marks); }

    public String getGrade()                     { return grade; }
    public void   setGrade(String grade)         { this.grade = grade; }
}
