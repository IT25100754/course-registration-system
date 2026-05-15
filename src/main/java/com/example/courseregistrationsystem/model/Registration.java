package com.example.courseregistrationsystem.model;

/**
 * Registration entity — central class in the UML class diagram.
 * Has aggregation with Course (◇) and association with Student (△ via inheritance).
 *
 * OOP Concepts:
 * - INHERITANCE: Extends BaseEntity
 * - ENCAPSULATION: All fields private
 * - POLYMORPHISM: Overrides toFileString(), getDisplayName()
 * - ASSOCIATION: Links Student and Course
 *
 * File format:
 *   id|registrationId|studentId|courseId|status|sectionId|studentType|enrolledDate|createdAt|updatedAt
 */
public class Registration extends BaseEntity {

    private String registrationId;
    private String studentId;
    private String courseId;
    private String status;       // CONFIRMED | WAITLISTED | DROPPED
    private String sectionId;
    private String studentType;  // undergraduate | graduate
    private String enrolledDate;

    // ─── Constructors ───────────────────────────────────────────────────────────

    public Registration() { super(); }

    public Registration(String id, String registrationId, String studentId,
                        String courseId, String status, String sectionId,
                        String studentType) {
        super(id);
        this.registrationId = registrationId;
        this.studentId      = studentId;
        this.courseId       = courseId;
        this.status         = status;
        this.sectionId      = sectionId != null ? sectionId : "Default";
        this.studentType    = studentType != null ? studentType : "undergraduate";
        this.enrolledDate   = java.time.LocalDateTime.now().toString();
    }

    // ─── Polymorphism ───────────────────────────────────────────────────────────

    @Override
    public String toFileString() {
        return String.join("|",
                getId(),
                registrationId,
                studentId,
                courseId,
                status,
                sectionId != null ? sectionId : "Default",
                studentType != null ? studentType : "undergraduate",
                enrolledDate != null ? enrolledDate : "",
                getCreatedAt(),
                getUpdatedAt()
        );
    }

    @Override
    public String getDisplayName() {
        return "Registration[" + registrationId + "] Student:" + studentId + " → Course:" + courseId;
    }

    public static Registration fromFileString(String line) {
        String[] parts = line.split("\\|", -1);
        if (parts.length < 6) throw new IllegalArgumentException("Invalid registration record: " + line);

        Registration r = new Registration();
        r.setId(parts[0]);
        r.setRegistrationId(parts[1]);
        r.setStudentId(parts[2]);
        r.setCourseId(parts[3]);
        r.setStatus(parts[4]);
        r.setSectionId(parts[5]);
        if (parts.length > 6) r.setStudentType(parts[6]);
        if (parts.length > 7) r.setEnrolledDate(parts[7]);
        if (parts.length > 8) r.setCreatedAt(parts[8]);
        if (parts.length > 9) r.setUpdatedAt(parts[9]);
        return r;
    }

    // ─── UML Methods: confirmRegistration, cancelRegistration, getDetails ───────

    public void confirmRegistration() {
        this.status = "CONFIRMED";
        updateTimestamp();
    }

    public void cancelRegistration() {
        this.status = "DROPPED";
        updateTimestamp();
    }

    public String getDetails() {
        return String.format("Registration ID: %s | Student: %s | Course: %s | Status: %s | Section: %s",
                registrationId, studentId, courseId, status, sectionId);
    }

    // ─── Getters & Setters ───────────────────────────────────────────────────────

    public String getRegistrationId()                        { return registrationId; }
    public void   setRegistrationId(String registrationId)   { this.registrationId = registrationId; }

    public String getStudentId()                             { return studentId; }
    public void   setStudentId(String studentId)             { this.studentId = studentId; }

    public String getCourseId()                              { return courseId; }
    public void   setCourseId(String courseId)               { this.courseId = courseId; }

    public String getStatus()                                { return status; }
    public void   setStatus(String status)                   { this.status = status; }

    public String getSectionId()                             { return sectionId; }
    public void   setSectionId(String sectionId)             { this.sectionId = sectionId; }

    public String getStudentType()                           { return studentType; }
    public void   setStudentType(String studentType)         { this.studentType = studentType; }

    public String getEnrolledDate()                          { return enrolledDate; }
    public void   setEnrolledDate(String enrolledDate)       { this.enrolledDate = enrolledDate; }
}
